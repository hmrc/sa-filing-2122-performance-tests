/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

///*
// * Copyright 2022 HM Revenue & Customs
// *
// */
//
//package uk.gov.hmrc.perftests.safiling
//
//
//import io.gatling.commons.stats.KO
//import io.gatling.http.request.ExtraInfo
//
//
//class ExtraInfoExtractor {
//  def dumpOnFailure(extraInfo: ExtraInfo): List[Any] = {
//    if (extraInfo.status.eq(KO)) {
//      List(s"\nURL: [${extraInfo.request.getUrl}]. " +
//        s"Status Code: [${extraInfo.response.statusCode}]. " +
//        s"Request: [${extraInfo.request.getStringData}]. " +
//        s"Response Header: [${extraInfo.response.headers}]. " +
//        s"Response: [${extraInfo.response.body.string}]. " +
//        s"UtrsKO: ${if(extraInfo.request.getUrl.split("/").length>6)extraInfo.request.getUrl.split("/")(6)}\n")
//    } else {
//      List(s"\nUtrsOK: ${if(extraInfo.request.getUrl.split("/").length>6)extraInfo.request.getUrl.split("/")(6)}\n")
//    }
//  }
//}
//
//object ExtraInfoExtractor extends ExtraInfoExtractor
