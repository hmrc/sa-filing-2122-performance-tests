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
import io.gatling.http.Predef._

import scala.util.Random

object SignInRequests extends SAUrlsConfiguration {

  val loginStubUrl = s"$baseUrl/auth-login-stub/gg-sign-in"
  val loginUrl: String = s"$baseUrl/gg/sign-in?continue=/$saUtrPrefix/return/welcome"
  def getAuthSignInPage =
    http("GET - GG Start")
      .get(loginStubUrl)
      .check(status.is(200))
      .check(currentLocation.is(loginStubUrl))

  def postAuthLoginWithSaOnly(confidenceLevel: Int) = postAuthLoginToWelcome(saFormParamsMap(), confidenceLevel)

  def postAuthLoginToWelcome(enrolmentParams: Map[String, String], confidenceLevel: Int) = {
    http("POST - GG Login")
      .post(loginStubUrl)
      .disableFollowRedirect
      .formParamMap(authLoginStubStandardParams(confidenceLevel))
      .formParam("oauthTokens.idToken", Random.alphanumeric.take(10).mkString(""))
      .formParam("redirectionUrl", welcomeUrl)
      .formParamMap(enrolmentParams)
      .check(status.is(303))
  }


  def saFormParamsMap(): Map[String, String] = {
    val formField = "enrolment[0]."
    Map(formField + "name" -> "IR-SA",
      formField + "taxIdentifier[0].name" -> "UTR",
      formField + "taxIdentifier[0].value" -> s"$${saUtr}",
      formField + "state" -> "Activated")
  }

  def authLoginStubStandardParams(confidenceLevel: Int): Map[String, String] = {
    val nino = if (confidenceLevel == 200) "AB123456C" else ""
    val affinityGroup = if (confidenceLevel == 200) "Individual" else "Organisation"
    Map(
      "authorityId" -> "",
      "confidenceLevel" -> confidenceLevel.toString,
      "credentialStrength" -> "strong",
      "nino" -> nino,
      "affinityGroup" -> affinityGroup)
  }

}
