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

package uk.gov.hmrc.vatsubscription.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ChangeIndicators(ppob: Boolean,
                            bankDetails: Boolean,
                            returnPeriod: Boolean,
                            deregister: Boolean,
                            annualAccounting: Boolean)

object ChangeIndicators {
  private val ppobDetailsPath = JsPath \ "PPOBDetails"
  private val bankDetailsPath =  JsPath \ "bankDetails"
  private val returnPeriodPath = JsPath \ "returnPeriod"
  private val deregisterPath = JsPath \ "deregister"
  private val annualAccountingPath = JsPath \ "annualAccounting"

  implicit val changeIndicatorsReader: Reads[ChangeIndicators] = for {
    ppob <- ppobDetailsPath.read[Boolean]
    bankDetails <- bankDetailsPath.read[Boolean]
    returnPeriod <- returnPeriodPath.read[Boolean]
    deregister <- deregisterPath.read[Boolean]
    annualAccounting <- annualAccountingPath.read[Boolean] or Reads.pure(false)
  } yield ChangeIndicators(
    ppob,
    bankDetails,
    returnPeriod,
    deregister,
    annualAccounting
  )

  implicit val changeIndicatorsWriter: Writes[ChangeIndicators] = (
    ppobDetailsPath.write[Boolean] and
    bankDetailsPath.write[Boolean] and
    returnPeriodPath.write[Boolean] and
    deregisterPath.write[Boolean] and
    annualAccountingPath.write[Boolean]
  )(unlift(ChangeIndicators.unapply))
}
