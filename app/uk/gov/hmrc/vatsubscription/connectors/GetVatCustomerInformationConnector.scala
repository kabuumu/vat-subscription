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

package uk.gov.hmrc.vatsubscription.connectors

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK, FORBIDDEN, PRECONDITION_FAILED}
import play.api.libs.json.{JsSuccess, Json, Writes}
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._
import uk.gov.hmrc.vatsubscription.config.AppConfig
import uk.gov.hmrc.vatsubscription.models.VatCustomerInformation

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GetVatCustomerInformationConnector @Inject()(val http: HttpClient,
                                                   val applicationConfig: AppConfig) {

  private def url(vatNumber: String) = s"${applicationConfig.desUrl}/vat/customer/vrn/$vatNumber/information"

  def getInformation(vatNumber: String)(implicit hc: HeaderCarrier): Future[GetVatCustomerInformationHttpParser.GetVatCustomerInformationHttpParserResponse] = {
    val headerCarrier = hc
      .withExtraHeaders(applicationConfig.desEnvironmentHeader)
      .copy(authorization = Some(Authorization(applicationConfig.desAuthorisationToken)))

    Logger.debug(s"[GetVatCustomerInformationConnector][getInformation] URL: ${url(vatNumber)}")
    Logger.debug(s"[GetVatCustomerInformationConnector][getInformation] Headers: ${headerCarrier.headers}")

    http.GET[GetVatCustomerInformationHttpParser.GetVatCustomerInformationHttpParserResponse](
      url = url(vatNumber)
    )(
      GetVatCustomerInformationHttpParser.GetVatCustomerInformationHttpReads,
      headerCarrier,
      implicitly[ExecutionContext]
    )
  }

  //scalastyle:off
  object GetVatCustomerInformationHttpParser {
    type GetVatCustomerInformationHttpParserResponse = Either[GetVatCustomerInformationFailure, VatCustomerInformation]

    implicit object GetVatCustomerInformationHttpReads extends HttpReads[GetVatCustomerInformationHttpParserResponse] {
      override def read(method: String, url: String, response: HttpResponse): GetVatCustomerInformationHttpParserResponse =
        response.status match {
          case OK =>
            Logger.debug("[CustomerCircumstancesHttpParser][read]: Status OK")
            response.json.validate(
              if(applicationConfig.features.latestApi1363Version()) VatCustomerInformation.newReads
              else VatCustomerInformation.currentReads
            ) match {
              case JsSuccess(vatCustomerInformation, _) =>
                Logger.debug(s"[CustomerCircumstancesHttpParser][read]: Json Body: \n\n${response.body}")
                Right(vatCustomerInformation)
              case _ =>
                Logger.warn(s"[CustomerCircumstancesHttpParser][read]: Invalid Success Response Json")
                Left(UnexpectedGetVatCustomerInformationFailure(OK, response.body))
            }
          case BAD_REQUEST =>
            Logger.warn("[CustomerCircumstancesHttpParser][read]: Unexpected response, status BAD REQUEST returned")
            Left(InvalidVatNumber)
          case NOT_FOUND =>
            Logger.warn("[CustomerCircumstancesHttpParser][read]: Unexpected response, status NOT FOUND returned")
            Left(VatNumberNotFound)
          case FORBIDDEN if response.body.contains("MIGRATION") =>
            Logger.warn("[CustomerCircumstancesHttpParser][read]: Unexpected response, " +
              "status FORBIDDEN returned with MIGRATION")
            Left(Migration)
          case FORBIDDEN =>
            Logger.warn("[CustomerCircumstancesHttpParser][read]: Unexpected response, status FORBIDDEN returned")
            Left(Forbidden)
          case status =>
            Logger.warn(s"[CustomerCircumstancesHttpParser][read]: Unexpected response, status $status returned")
            Left(UnexpectedGetVatCustomerInformationFailure(status, response.body))
        }
    }
  }
}
//scalastyle:on

sealed trait GetVatCustomerInformationFailure {
  val status: Int = INTERNAL_SERVER_ERROR
  val body: String
}

object GetVatCustomerInformationFailure {
  implicit val writes: Writes[GetVatCustomerInformationFailure] = Writes {
    error => Json.obj("status" -> error.status, "body" -> error.body)
  }
}

case object InvalidVatNumber extends GetVatCustomerInformationFailure {
  override val body = "Invalid vat number"
}

case object VatNumberNotFound extends GetVatCustomerInformationFailure {
  override val body = "Vat number not found"
}

case object Forbidden extends GetVatCustomerInformationFailure {
  override val status: Int = FORBIDDEN
  override val body: String = ""
}

case object Migration extends GetVatCustomerInformationFailure {
  override val status: Int = PRECONDITION_FAILED
  override val body: String = "Migration"
}

case class UnexpectedGetVatCustomerInformationFailure(override val status: Int, override val body: String)
  extends GetVatCustomerInformationFailure
