/*
 * Copyright 2022 HM Revenue & Customs
 *
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
