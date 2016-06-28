name := """interaction"""

organization := "com.dodocool.website"

version := "1.0-SNAPSHOT"

lazy val interaction = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.tomtop.website" % "order_api_2.11" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "interaction_api_2.11" % "1.0-SNAPSHOT"
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
