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

import uk.gov.hmrc.perftests.safiling.ReAuthSteps._
import uk.gov.hmrc.perftests.safiling.ScenarioConfiguration._

class SaFilingSimulation extends PerformanceTestRunner2 {

  Seq("TC2", "TC3", "TC33", "TC45", "TC74", "TC81", "TC91", "TC95").foreach { name =>
    setup(s"journey$name", s"Journey for $name")
      .withRequests(createScenarioRequests(s"src/test/resources/data/journey/$name.json", 1.0):_*)
      .withRequests(individualContinueToReAuthSteps:_*)
      .withRequests(submitWithStubSteps:_*)
  }

  runSimulation()

}
