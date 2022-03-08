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
