name := """tracking"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
    javaWs,
    "com.tomtop.website" % "tracking_api_2.11" % "1.1-SNAPSHOT"
)   





