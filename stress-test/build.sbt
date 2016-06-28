

lazy val stress = (project in file("."))
                   .enablePlugins(GatlingPlugin)

libraryDependencies ++= Seq(
	"io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.5",
	"io.gatling"            % "gatling-test-framework"    % "2.1.5"
)

