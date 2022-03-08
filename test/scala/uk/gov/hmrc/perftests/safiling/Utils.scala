/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling

import io.gatling.core.Predef._

trait Utils {

  val versionPattern = """<input type="hidden" name="version"\s+value="([^"]+)""""
  def saveVersion() = regex(_ => versionPattern).find.optional.saveAs("version")

  val continuePattern = """<a href="([^"]+)">Continue"""
  def saveContinueUrl() = regex(_ => continuePattern).find.saveAs("continue")

}
