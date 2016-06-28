name := """base_api"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val base_api = (project in file("."))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.maxmind.geoip2" % "geoip2" % "2.1.0",
  "com.github.napp-com" % "kaptcha" % "1.0.0",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "commons-io" % "commons-io" % "2.2",
  "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
  "com.google.guava" % "guava" % "16.0.1",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "com.typesafe.play" % "play-mailer_2.11" % "3.0.0-M1",
  "javax.mail" % "mail" % "1.5.0-b01",
  "javax.activation" % "activation" % "1.1.1",
  "org.apache.poi" % "poi-ooxml" % "3.12-beta1"
)

sources in (Compile,doc) := Seq.empty

javacOptions ++= Seq("-encoding", "UTF-8")

publishArtifact in (Compile, packageDoc) := false

publishTo := {
  val repo = "http://192.168.220.54:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local") 
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))

