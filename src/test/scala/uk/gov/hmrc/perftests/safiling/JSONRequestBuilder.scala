/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.safiling

import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.http.Predef.{http, status, _}
import io.gatling.http.request.builder.HttpRequestBuilder

trait JSONRequestBuilder extends Utils with SAUrlsConfiguration{
  def buildJourneyRequests(fragment: ScenarioFragment, fileName: String, percentage: Double): Seq[HttpRequestBuilder] = {
    printNumberOfPages(fragment.scenarioRequests.length, fileName, percentage)
    fragment.scenarioRequests.map(createRequestBuilder)
  }

  private def createRequestBuilder(p: PortalRequest): HttpRequestBuilder = {
    val fullUrl: String = s"$saUrl${p.url}"
    val pageUrl: Expression[String] = fullUrl
    val title: String = s"${p.method} on ${p.url}"
    val h = http(title)
    (p.method match {
      case "GET" => h.get(pageUrl)
        .disableFollowRedirect
        //.extraInfoExtractor(dumpOnFailure)

      case "POST" => {
        p.body.getOrElse(Map()).foldLeft(h.post(pageUrl)
          .disableFollowRedirect
          //.extraInfoExtractor(dumpOnFailure)
        ){
          (e, p) => e.formParam(p._1, p._2)
        }
      }
    }).check(status.is(p.responseStatus))
  }

  private def printNumberOfPages(pages: Int,  fileName: String, percentage: Double): Unit = {
    //println(s"Journey: ${fileName} has ${pages} requests")
    val expectedSubmissions = 30000
    val approxSubmissionsForJourney = expectedSubmissions * percentage
    val authAndContingency = 25
    val tps = ((approxSubmissionsForJourney * (pages + authAndContingency)) / 60) / 60
    val jps = tps / pages
    //println(s"with an expected TPS of: ${tps} and JPS of: ${jps}")
  }
}
