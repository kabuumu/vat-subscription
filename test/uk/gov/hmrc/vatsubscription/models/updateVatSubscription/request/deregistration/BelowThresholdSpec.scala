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

package uk.gov.hmrc.vatsubscription.models.updateVatSubscription.request.deregistration

import play.api.libs.json.{JsError, JsString, Json}
import uk.gov.hmrc.play.test.UnitSpec

class BelowThresholdSpec extends UnitSpec {

  "BelowThreshold" when {

    "deserializing JSON" should {

      "return BelowNext12Months case object for 'belowNext12Months'" in {
        JsString("belowNext12Months").as[BelowThreshold] shouldBe BelowNext12Months
      }

      "return BelowPast12Months case object for 'belowPast12Months'" in {
        JsString("belowPast12Months").as[BelowThreshold] shouldBe BelowPast12Months
      }

      "return JsError invalid" in {
        JsString("banana").validate[BelowThreshold].isError shouldBe true
      }
    }

    "serializing to JSON" should {

      "for BelowPast12Months output '3'" in {
        Json.toJson(BelowPast12Months) shouldBe JsString("3")
      }

      "for BelowNext12Months output '2'" in {
        Json.toJson(BelowNext12Months) shouldBe JsString("2")
      }
    }
  }
}
