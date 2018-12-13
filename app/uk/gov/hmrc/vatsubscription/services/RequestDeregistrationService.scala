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

package uk.gov.hmrc.vatsubscription.services

import javax.inject.{Inject, Singleton}

import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.vatsubscription.connectors.UpdateVatSubscriptionConnector
import uk.gov.hmrc.vatsubscription.httpparsers.UpdateVatSubscriptionHttpParser.UpdateVatSubscriptionResponse
import uk.gov.hmrc.vatsubscription.models.{ContactDetails, User}
import uk.gov.hmrc.vatsubscription.models.updateVatSubscription.request._
import uk.gov.hmrc.vatsubscription.models.updateVatSubscription.request.deregistration.DeregistrationInfo

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RequestDeregistrationService @Inject()(updateVatSubscriptionConnector: UpdateVatSubscriptionConnector) {

  def deregister(deregistration: DeregistrationInfo, welshIndicator: Boolean)
                (implicit user: User[_], hc: HeaderCarrier, ec: ExecutionContext): Future[UpdateVatSubscriptionResponse] = {

    val subscriptionModel = constructDeregistrationModel(deregistration, welshIndicator)
    Logger.debug(s"[RequestDeregistrationService][deregister] Submitting Deregistration Request for user with vrn - ${user.vrn}")
    updateVatSubscriptionConnector.updateVatSubscription(user, subscriptionModel, hc)
  }

  def constructDeregistrationModel(deregistration: DeregistrationInfo, welshIndicator: Boolean)
                                  (implicit user: User[_]): UpdateVatSubscription = {

    val agentContactDetails: Option[ContactDetails] =
      if(deregistration.transactorOrCapacitorEmail.isDefined)
        Some(ContactDetails(None, None, None, deregistration.transactorOrCapacitorEmail, None))
      else
        None

    val agentOrCapacitor: Option[AgentOrCapacitor] = user.arn.map(AgentOrCapacitor(_, agentContactDetails))

    UpdateVatSubscription(
      controlInformation = ControlInformation(welshIndicator),
      requestedChanges = DeregistrationRequest,
      updatedPPOB = None,
      updatedReturnPeriod = None,
      updateDeregistrationInfo = Some(deregistration),
      declaration = Declaration(agentOrCapacitor, Signing())
    )
  }
}
