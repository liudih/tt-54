name := """manager"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val manager = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "com.tomtop.website.activity" %% "activity_api" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "member" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "product" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "product_services" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "order" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "interaction" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "loyalty" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "advertising" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "paypal" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "tracking" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "messaging" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "livechat" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "wholesale" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "mobile" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "base" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "google" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "facebook" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "payment" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "shareasale" % "1.0-SNAPSHOT"
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

publishTo := {
  val repo = "http://192.168.7.15:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local")
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))

