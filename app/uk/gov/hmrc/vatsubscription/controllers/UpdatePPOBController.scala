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

package uk.gov.hmrc.vatsubscription.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.vatsubscription.controllers.actions.VatAuthorised
import uk.gov.hmrc.vatsubscription.models.User
import uk.gov.hmrc.vatsubscription.models.post.PPOBPost
import uk.gov.hmrc.vatsubscription.services.{UpdatePPOBService, VatCustomerDetailsRetrievalService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UpdatePPOBController @Inject()(VatAuthorised: VatAuthorised,
                                     updatePPOBService: UpdatePPOBService,
                                     vatCustomerDetailsRetrievalService: VatCustomerDetailsRetrievalService)
                                    (implicit ec: ExecutionContext) extends MicroserviceBaseController {

  def updatePPOB(vrn: String): Action[AnyContent] = VatAuthorised.async(vrn) {
    implicit user =>
        parseJsonBody[PPOBPost] match {
          case Right(updatedPPOB) =>
            for {
              welshIndicator <- vatCustomerDetailsRetrievalService.extractWelshIndicator(vrn)
              result <- updatePPOBService.updatePPOB(updatedPPOB, welshIndicator)
            } yield result match {
              case Right(success) => Ok(Json.toJson(success))
              case Left(error) => InternalServerError(Json.toJson(error))
            }
          case Left(error) =>
            Future.successful(BadRequest(Json.toJson(error)))
        }
      }
}




