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

package uk.gov.hmrc.vatsubscription.config.featureSwitch

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Features @Inject()(config: Configuration) {

  private val featureSwitch: String = "feature-switch"
  lazy val latestApi1363Version = new Feature(s"$featureSwitch.latestApi1363Version", config)
  lazy val latestApi1365Version = new Feature(s"$featureSwitch.latestApi1365Version", config)
  lazy val stubDes = new Feature(s"$featureSwitch.stubDes", config)

}