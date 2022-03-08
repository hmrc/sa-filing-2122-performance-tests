/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.safiling


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.conf.Configuration

object HttpConfiguration2 extends Configuration {

  val headers = Map(
    """Accept""" -> """text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8""",
    """Cache-Control""" -> """no-cache""")

  val httpProtocol = http
    .acceptHeader("image/png,image/*;q=0.8,*/*;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-gb,en;q=0.5")
    .connectionHeader("close")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0")
    .header("True-Client-IP", s"$${random}")
    //.shareConnections
}
