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

trait Utils {

  val versionPattern = """<input type="hidden" name="version"\s+value="([^"]+)""""
  def saveVersion() = regex(_ => versionPattern).find.optional.saveAs("version")

  val continuePattern = """<a class="govuk-button" href="([^"]+)">Continue"""
  def saveContinueUrl() = regex(_ => continuePattern).find.saveAs("continue")

}
