name := """activity_api"""

version := "1.0-SNAPSHOT"

organization := "com.tomtop.website.activity"

scalaVersion := "2.11.1"

lazy val activity_api = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaWs,
  "com.tomtop.website" %% "base_api" % "1.0-SNAPSHOT"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
// routesGenerator := InjectedRoutesGenerator

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