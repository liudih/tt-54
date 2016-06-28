name := """research"""

version := "1.0-SNAPSHOT"

organization := "com.tomtop.website.activity"

scalaVersion := "2.11.1"

lazy val research = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaWs,
  "com.tomtop.website" %% "member_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "order_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "loyalty_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "product_api" % "1.1-SNAPSHOT",
  "com.google.apis" % "google-api-services-content" % "v2-rev55-1.19.0",
  "com.google.api-client" % "google-api-client" % "1.19.0"
)


LessKeys.verbose in Assets := true

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

javacOptions ++= Seq("-encoding", "UTF-8")

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