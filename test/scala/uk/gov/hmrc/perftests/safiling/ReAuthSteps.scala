/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.apache.commons.lang.StringEscapeUtils
import uk.gov.hmrc.perftests.safiling.ScenarioConfiguration._


  object ReAuthSteps extends SAUrlsConfiguration {
    val portalReauthUrl = s"$baseUrl/portal-reauthentication/"
    val portalReAuthValidateUrl = s"$baseUrl/portal-reauthentication/validate"
    val primeReAuthWithRedirectUrl = s"$baseUrl/reauthentication/?continue=/portal-reauthentication/validate"
    val reauthLoginUrl: String = s"$baseUrl/reauthentication/login"
    val reauthStubLoginUrl: String = s"$baseUrl/reauthentication/test-only/complete"
    val submissionConfirmationUrl: String = s"$saUrl/return/submit/submitting"
    val getIndividualDeclarationUrl: String = s"$saUrl/return/submit/individualDeclaration"
    val postIndividualDeclarationUrl: String = s"$saUrl/return/submit/individualDeclaration?httpmethod=put"
    var failureURL: String = s"$baseUrl/2122/security-check-failed"
    //var successURL: String = s"$saUrl/return/submit/submitting?httpmethod=put&version=$${version}"

    val getIndividualDeclaration = http("GET submit/individualDeclaration")
      .get(getIndividualDeclarationUrl)
      //.extraInfoExtractor(dumpOnFailure)
      .disableFollowRedirect
      .check(saveVersion())
      .check(status.lt(400))

    val continueToReAuth = http("Individual POST submit/individualDeclaration")
      .post(postIndividualDeclarationUrl)
      .disableFollowRedirect
      //.extraInfoExtractor(dumpOnFailure)
      .formParam("submitDeclaration", "true")
      .formParam("_submitDeclaration", "")
      .formParam("redirectUrl", "")
      .formParam("next", "Next")
      .formParam("version", s"$${version}")
      .check(savePortalReAuthPayload())
      .check(saveSuccessUrl())
      .check(status.lt(400))
      //.extraInfoExtractor(dumpOnFailure)


    val doPrimePortalReauthenticationHttp = http("POST portal-reauthentication")
      .post(portalReauthUrl)
      //.extraInfoExtractor(dumpOnFailure)
      .formParam("successURL", s"$${successURL}")
      .formParam("abandonURL", getIndividualDeclarationUrl)
      .formParam("failureURL", failureURL)
      .formParam("payload", s"$${payload}")
      .disableFollowRedirect
      .check(status is 303)
      .check(header("Location").saveAs("reauthenticationLocation"))

    val getPortalReauthenticationValidateHttp = http("Portal reauthentication validate")
      .get(s"$baseUrl$${reauthenticationLocation}")
      .disableFollowRedirect
      .check(status.lt(400))

    //need to pause 2 seconds
    val getReauthenticationStubLoginPageHttp = http("GET test-only/complete")
      .get(reauthStubLoginUrl)
      //.extraInfoExtractor(dumpOnFailure)
      .disableFollowRedirect
      .check(saveContinueUrl())
      //.check(status lessThan 400)

    val getPortalReauthValidateHttp = http("GET portal-reauthentication/validate")
      .get(s"$baseUrl$${continue}")
      //.extraInfoExtractor(dumpOnFailure)
      //.check(status lessThan 400)
      .check(saveSuccessUrl())
      .check(saveWResult())
      .check(saveReAuthResponseType())

    val submitFileReturnHttp = http("Submit file return")
      .post(s"$${successURL}")
      //.extraInfoExtractor(dumpOnFailure)
      .disableFollowRedirect
      .formParam("successURL", s"$${successURL}")
      .formParam("failureURL", failureURL)
      .formParam("abandonURL", getIndividualDeclarationUrl)
      .formParam("addCredentials", "")
      .formParam("wresult", s"$${wresult}")
      .formParam("reauthResponseType", s"$${reauthResponseType}")
      .check(status.lt(400))

    val getSubmissionConfirmationPageHttp = http("Submission confirmation page")
      .get(submissionConfirmationUrl)
      //.extraInfoExtractor(dumpOnFailure)
      .disableFollowRedirect
      //.check(submissionIdIsPresent().exists)
      .check(status.lt(400))

    val individualContinueToReAuthSteps =
      List(getIndividualDeclaration,continueToReAuth)

    val submitWithStubSteps =
      List(doPrimePortalReauthenticationHttp,
        getPortalReauthenticationValidateHttp,
        getReauthenticationStubLoginPageHttp,
        getPortalReauthValidateHttp,
        submitFileReturnHttp)


    def saveCsrfToken() = regex(_ => csrfPattern).saveAs("csrfToken")
    def savePortalReAuthPayload() = regex(_ => payloadPattern).saveAs("payload")
    def saveSuccessUrl() = regex(_ => successUrlPattern).transform(StringEscapeUtils.unescapeXml(_)).saveAs("successURL")
    def saveAbandonUrl() = regex(_ => abandonUrlPattern).saveAs("abandonURL")
    def saveFailureUrl() = regex(_ => failureUrlPattern).saveAs("failureURL")
    def saveWResult() = regex(_ => wresultPattern).transform(StringEscapeUtils.unescapeXml(_)).saveAs("wresult")
    def saveReAuthResponseType() = regex(_ => reauthResponseTypePattern).saveAs("reauthResponseType")
    def submissionIdIsPresent() = regex(_ => submissionIdPattern)

    val csrfPattern = """<input type="hidden" name="csrfToken"\s+value="([^"]+)""""
    val payloadPattern = """<input type="hidden" name="payload"\s+value='(([^"]+|\n)+)'"""
    val successUrlPattern = """<input type="hidden" name="successURL"\s+value='([^"]+)'"""
    val abandonUrlPattern = """<input type="hidden" name="abandonURL"\s+value='([^"]+)'"""
    val failureUrlPattern = """<input type="hidden" name="failureURL"\s+value='([^"]+)'"""
    val wresultPattern = """<input type="hidden" name="wresult"\s+value='(([^"]+|\n)+)'"""
    val reauthResponseTypePattern = """<input type="hidden" name="reauthResponseType"\s+value='([^"]+)'"""
    val submissionIdPattern = """<div id="wf80_13_DataSubReceiptRefNumberInfo">"""
}
