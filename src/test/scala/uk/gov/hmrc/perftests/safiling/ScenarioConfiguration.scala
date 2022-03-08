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

import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.perftests.safiling.ScenarioFragmentJSONParser.loadDataFromFile
import uk.gov.hmrc.perftests.safiling.SignInRequests._

object ScenarioConfiguration extends JSONRequestBuilder {

  def createScenarioRequests(journeyFileName: String, percentage: Double, confidenceLevel: Int = 50): Seq[HttpRequestBuilder] =
    Seq(
      getAuthSignInPage,
      postAuthLoginWithSaOnly(confidenceLevel)
    ) ++ buildJourneyRequests(
      loadDataFromFile(journeyFileName),
      journeyFileName,
      percentage
    )

}
