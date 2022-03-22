import sbt._

object Dependencies {

  private val gatlingVersion = "3.4.2"

  val test = Seq(
    "io.gatling"               % "gatling-test-framework"     % gatlingVersion,
    "io.gatling.highcharts"    % "gatling-charts-highcharts"  % gatlingVersion,
    "com.typesafe"             % "config"                     % "1.3.1",
    "uk.gov.hmrc"             %% "performance-test-runner"    % "5.2.0",
    "org.jsoup"                % "jsoup"                      % "1.9.1",
    "commons-lang"             % "commons-lang"               % "2.6"
  )
}