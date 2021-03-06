/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.vatsubscription.models.post

import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.vatsubscription.helpers.PPOBTestConstants._

class PPOBAddressPostSpec extends UnitSpec {

  "PPOBAddressPost Reads" should {
    "parse the json correctly" in {
      ppobAddressPostJson.as[PPOBAddressPost] shouldBe ppobAddressPostValue
    }
  }

  "PPOBAddressPost Writes" should {
    "output a populated PPOBAddressGet model" in {
      Json.toJson(ppobAddressPostValue) shouldBe ppobAddressPostWritesResult
    }
  }

}
