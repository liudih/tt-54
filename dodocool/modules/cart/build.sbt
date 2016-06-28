name := """cart"""

organization := "com.dodocool.website"

version := "1.0-SNAPSHOT"

lazy val cart = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.tomtop.website" % "order_api_2.11" % "1.0-SNAPSHOT"
)

sources in (Compile,doc) := Seq.empty

