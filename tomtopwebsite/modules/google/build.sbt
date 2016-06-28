val foo=0

name := """google"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val google = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.apis" % "google-api-services-content" % "v2-rev55-1.20.0",
  "com.google.api-client" % "google-api-client" % "1.20.0",
  "com.thoughtworks.xstream" % "xstream" % "1.4.8",
  "org.apache.httpcomponents" % "httpclient" % "4.3.1"
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

publishTo := {
  val repo = "http://192.168.220.54:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local")
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))

