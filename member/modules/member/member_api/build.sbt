name := """member_api"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val member_api = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.tomtop.website" %% "base_api" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing()
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

