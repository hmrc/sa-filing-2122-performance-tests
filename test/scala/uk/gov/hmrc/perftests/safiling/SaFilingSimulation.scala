/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.safiling.ReAuthSteps._
import uk.gov.hmrc.perftests.safiling.ScenarioConfiguration._

class SaFilingSimulation extends PerformanceTestRunner {

  Seq("TC2", "TC3", "TC33", "TC45", "TC74", "TC81", "TC91", "TC95").foreach { name =>
    setup(s"journey$name", s"Journey for $name")
      .withRequests(createScenarioRequests(s"src/test/resources/data/journey/$name.json", 1.0):_*)
      .withRequests(individualContinueToReAuthSteps:_*)
      .withRequests(submitWithStubSteps:_*)
  }

  runSimulation()

}
