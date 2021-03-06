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

package uk.gov.hmrc.vatsubscription.helpers

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.vatsubscription.models.{MAReturnPeriod, MTDfBMandated, MandationStatus, NonMTDfB}
import uk.gov.hmrc.vatsubscription.models.updateVatSubscription.request._
import uk.gov.hmrc.vatsubscription.models.updateVatSubscription.response.{ErrorModel, SuccessModel}

object UpdateVatSubscriptionTestConstants {

  val messageType: String = "SubscriptionUpdate"

  val changeAll: RequestedChanges = RequestedChanges(ppobDetails = true, returnPeriod = true, deregInfo = true)

  val updatedReturnPeriod: UpdatedReturnPeriod = UpdatedReturnPeriod(MAReturnPeriod(Some("agent@emailaddress")))
  val updatedPPOB: UpdatedPPOB = UpdatedPPOB(PPOBTestConstants.ppobModelMaxPost)
  val updatedMandationStatus: MandationStatus = NonMTDfB

  val updateSuccessResponse: SuccessModel = SuccessModel("XAVV0000000123456")
  val updateErrorResponse: ErrorModel = ErrorModel("TEST","ERROR")
  val updateConflictResponse: ErrorModel = ErrorModel("CONFLICT","ERROR")

  val updateVatSubscriptionModelMax: UpdateVatSubscription = UpdateVatSubscription(
    controlInformation = ControlInformation(welshIndicator = false, mandationStatus = Some(MTDfBMandated)),
    requestedChanges = changeAll,
    updatedPPOB = Some(updatedPPOB),
    updatedReturnPeriod = Some(updatedReturnPeriod),
    updateDeregistrationInfo = Some(DeregistrationInfoTestConstants.deregInfoCeasedTradingModel),
    declaration = DeclarationTestConstants.declarationModelAgent
  )

  val updateVatSubscriptionLatestDESApi1365JsonMax: JsValue = Json.obj(
    "messageType" -> messageType,
    "controlInformation" -> ControlInformation(welshIndicator = false, mandationStatus = Some(MTDfBMandated)),
    "requestedChange" -> Json.toJson(changeAll)(RequestedChanges.DESApi1365WritesR11),
    "contactDetails" -> Json.toJson(updatedPPOB),
    "returnPeriods" -> Json.toJson(updatedReturnPeriod),
    "deregistrationInfo" -> DeregistrationInfoTestConstants.deregInfoCeasedTradingDESJson,
    "declaration" -> DeclarationTestConstants.declarationDESJsonAgent
  )

  val updateVatSubscriptionCurrentDESApi1365JsonMax: JsValue = Json.obj(
    "messageType" -> messageType,
    "controlInformation" -> ControlInformation(welshIndicator = false, mandationStatus = Some(MTDfBMandated)),
    "requestedChange" -> Json.toJson(changeAll)(RequestedChanges.DESApi1365WritesR7),
    "contactDetails" -> Json.toJson(updatedPPOB),
    "returnPeriods" -> Json.toJson(updatedReturnPeriod),
    "deregistrationInfo" -> DeregistrationInfoTestConstants.deregInfoCeasedTradingDESJson,
    "declaration" -> DeclarationTestConstants.declarationDESJsonAgent
  )

  val updateVatSubscriptionModelMin: UpdateVatSubscription = UpdateVatSubscription(
    controlInformation = ControlInformation(welshIndicator = false),
    requestedChanges = ChangeReturnPeriod,
    updatedPPOB = None,
    updatedReturnPeriod = Some(updatedReturnPeriod),
    updateDeregistrationInfo = None,
    declaration = Declaration(None, Signing())
  )

  val updateVatSubscriptionLatestDESApi1365JsonMin: JsValue = Json.obj(
    "messageType" -> messageType,
    "controlInformation" -> ControlInformation(welshIndicator = false),
    "requestedChange" -> Json.toJson(ChangeReturnPeriod)(RequestedChanges.DESApi1365WritesR11),
    "returnPeriods" -> Json.toJson(updatedReturnPeriod),
    "declaration" -> DeclarationTestConstants.declarationDESJsonlNonAgent
  )

  val updateVatSubscriptionCurrentDESApi1365JsonMin: JsValue = Json.obj(
    "messageType" -> messageType,
    "controlInformation" -> ControlInformation(welshIndicator = false),
    "requestedChange" -> Json.toJson(ChangeReturnPeriod)(RequestedChanges.DESApi1365WritesR7),
    "returnPeriods" -> Json.toJson(updatedReturnPeriod),
    "declaration" -> DeclarationTestConstants.declarationDESJsonlNonAgent
  )

  val controlInformationJsonMax: JsValue = Json.obj(
    "welshIndicator" -> true,
    "source" -> "100",
    "mandationStatus" -> "1"
  )

  val controlInformationJsonMin: JsValue = Json.obj(
    "welshIndicator" -> true,
    "source" -> "100"
  )
}
