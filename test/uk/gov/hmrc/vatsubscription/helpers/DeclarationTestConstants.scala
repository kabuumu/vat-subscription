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

package uk.gov.hmrc.vatsubscription.helpers

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.vatsubscription.models.ContactDetails
import uk.gov.hmrc.vatsubscription.models.updateVatSubscription.request.{AgentOrCapacitor, Declaration, Signing}

object DeclarationTestConstants {

  val agentContactDetails: ContactDetails = ContactDetails(None, None, None, Some("agent@emailaddress.com"), None)

  val agentOrCapacitor: AgentOrCapacitor = AgentOrCapacitor("XAIT0000000000", Some(agentContactDetails))

  val declarationModelAgent: Declaration = Declaration(Some(agentOrCapacitor), Signing())
  val declarationModelNonAgent: Declaration = Declaration(None, Signing())

  val declarationDESJsonAgent: JsValue = Json.obj(
    "agentOrCapacitor" -> agentOrCapacitor,
    "signing" -> Signing()
  )
  val declarationDESJsonlNonAgent: JsValue = Json.obj(
    "signing" -> Signing()
  )

}
