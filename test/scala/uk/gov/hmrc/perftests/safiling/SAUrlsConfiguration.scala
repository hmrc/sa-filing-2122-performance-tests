/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling

import uk.gov.hmrc.performance.conf.ServicesConfiguration

trait SAUrlsConfiguration extends ServicesConfiguration {

  val baseUrl = baseUrlFor("sa-filing-2122-frontend")
  val saUtrPrefix: String = s"self-assessment-file/2122/ind/$${saUtr}"
  val welcomeUrl: String = s"/$saUtrPrefix/return/welcome"
  val saUrl: String = s"$baseUrl/$saUtrPrefix"

}