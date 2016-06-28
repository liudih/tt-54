name := """activitydb"""

version := "1.0-SNAPSHOT"

organization := "com.tomtop.website.activity"

scalaVersion := "2.11.1"

lazy val activitydb = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaWs
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
// routesGenerator := InjectedRoutesGenerator

LessKeys.verbose in Assets := true

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

javacOptions ++= Seq("-encoding", "UTF-8")

sources in (Compile,doc) := Seq.empty
