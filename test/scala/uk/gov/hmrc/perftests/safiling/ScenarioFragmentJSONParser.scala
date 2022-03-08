/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling

import play.api.libs.json.Json.{fromJson, parse}
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.io.Source

case class ScenarioFragment(scenarioRequests: Seq[PortalRequest])
case class PortalRequest(method: String, url: String, body: Option[Map[String, String]], responseStatus: Int, responseRedirect: String)

object ScenarioFragmentJSONParser {

  def loadDataFromFile(fileName: String): ScenarioFragment ={
    implicit val requestReads = Json.reads[PortalRequest]
    implicit val dataReads = Json.reads[ScenarioFragment]

    val file = Source.fromFile(fileName)
    val json = file.iter.mkString

    val dataMaybe = fromJson[ScenarioFragment](parse(json))

    dataMaybe match {
      case JsError(errors) => throw new RuntimeException(s"Encountered an error parsing json file $fileName: " + errors)
      case JsSuccess(d, p) => d
    }
  }
}

