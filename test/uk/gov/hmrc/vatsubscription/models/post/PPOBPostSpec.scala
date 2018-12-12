/*
 * Copyright 2018 HM Revenue & Customs
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

class PPOBPostSpec extends UnitSpec {

  "PPOBPost Reads" should {
    "parse the json correctly where there is an agent email" in {
      ppobPostExampleReadsJson.as[PPOBPost] shouldBe ppobPostExample
    }

    "parse the json correctly where there is no agent email" in {
      ppobPostExampleReadsNoAgentEmailJson.as[PPOBPost] shouldBe ppobPostNoAgentEmailExample
     }
  }

  "PPOBPost Writes" should {
    "output a populated PPOBPost model with an agent email" in {
      Json.toJson(ppobPostExample) shouldBe ppobPostExampleWritesJson
    }

    "output a populated PPOBPost model without an agent email" in {
      Json.toJson(ppobPostNoAgentEmailExample) shouldBe ppobPostExampleWritesJson
    }
  }
}
