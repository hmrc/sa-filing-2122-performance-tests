/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling

import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import uk.gov.hmrc.performance.conf.PerftestConfiguration
import uk.gov.hmrc.performance.simulation.{Journey, JourneyPart}
import uk.gov.hmrc.perftests.safiling.HttpConfiguration2._

import scala.util.Random


trait PerformanceTestRunner2 extends Simulation
  with JourneyConfiguration2
  with PerftestConfiguration {

  private val parts = scala.collection.mutable.MutableList[JourneyPart]()

  def setup(id: String, description: String): JourneyPart = {

    val part: JourneyPart = JourneyPart(id, description)
    parts += part
    part
  }

  val userFeeder = Iterator.continually(Map("version" -> "0", "saUtr" -> Seq.fill(10)(Random.nextInt(9)).mkString("")))

  private def journeys: Seq[Journey] = {

    println(s"Implemented journey parts: ${parts.map(_.id).mkString(", ")}")

    println(labels)

    definitions(labels).map(conf => {

      println(s"Setting up scenario '${conf.id}' to run at ${conf.load} JPS and load to $loadPercentage %")

      val partsInJourney = conf.parts.map(p => parts.find(_.id.trim == p.trim)
        .getOrElse(throw new IllegalArgumentException(s"Scenario '${conf.id}' is configured to run '$p' but there is no journey part for it in the code"))
      )

      val first = group(partsInJourney.head.description) {
        partsInJourney.head.builder
      }
      val chain = partsInJourney.tail.foldLeft(first)((c, p) => c.group(p.description) {
        p.builder
      })

      new Journey {

        lazy val feeder = conf.feeder
        val RNG = new Random

        override lazy val builder: ScenarioBuilder = {
          val scenarioBuilder =
            if (!feeder.isEmpty) scenario(conf.description).feed(userFeeder)
            else scenario(conf.description)
          scenarioBuilder
            .feed(Iterator.continually(Map("currentTime" -> System.currentTimeMillis().toString)))
            .feed(Iterator.continually(Map("random" -> Math.abs(RNG.nextInt()))))
            .exitBlockOnFail(exec(chain))
        }

        override lazy val load: Double = conf.load
      }

    })
  }

  private def withAtLeasOneRequestInTheFullTest(load: Double) = load match {
    case rate if (constantRateTime.toSeconds * rate).toInt < 1 => 1D / (constantRateTime.toSeconds - 1)
    case rate => rate
  }

  private def withInjectedLoad(journeys: Seq[Journey]): Seq[PopulationBuilder] = journeys.map(scenario => {

    val load = withAtLeasOneRequestInTheFullTest(scenario.load * loadPercentage)
    println(s"expected load: ${load}")

    val injectionSteps = List(
      rampUsersPerSec(noLoad).to(load).during(rampUpTime),
      constantUsersPerSec(load).during(constantRateTime),
      rampUsersPerSec(load).to(noLoad).during(rampDownTime)
    )

    scenario.builder.inject(injectionSteps)
  })

  def runSimulation(): Unit = {

    import scala.concurrent.duration._
    val timeoutAtEndOfTest: FiniteDuration = 5 minutes

    println(s"Setting up simulation ")

    if (runSingleUserJourney) {

      println(s"'perftest.runSmokeTest' is set to true, ignoring all loads and running with only one user per journey!")

      val injectedBuilders = journeys.map(scenario => {
        scenario.builder.inject(atOnceUsers(1))
      })

      setUp(injectedBuilders: _*)
        .protocols(httpProtocol)
        .assertions(global.failedRequests.count.is(0))
    } else {
      setUp(withInjectedLoad(journeys): _*)
        .maxDuration(rampUpTime + constantRateTime + rampDownTime + timeoutAtEndOfTest)
        .protocols(httpProtocol)
        .assertions(global.failedRequests.percent.lte(percentageFailureThreshold))
    }
  }
}
