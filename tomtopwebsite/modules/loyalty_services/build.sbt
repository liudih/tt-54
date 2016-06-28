name := """loyalty_services"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val loyalty_services = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "axis" % "axis" % "1.4",
  "commons-discovery" % "commons-discovery" % "0.5",
  "javax.xml" % "jaxrpc" % "1.1",
  "javax.xml.soap" % "saaj-api" % "1.3.5",
  "wsdl4j" % "wsdl4j" % "1.6.3",
  "javax.mail" % "mail" % "1.4.7",
  "activation" % "activation" % "1.0.2",
  "com.tomtop.website" %% "loyalty_api" % "1.1-SNAPSHOT"
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

