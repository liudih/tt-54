name := """tomtopwebsite-activity"""

organization := "com.tomtop.website.activity"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val base = (project in file("modules/base"))
	.enablePlugins(PlayJava)

lazy val activitydb = (project in file("modules/activitydb"))
	.enablePlugins(PlayJava).dependsOn(base)
	
lazy val activity_api = (project in file("modules/activity_api"))
	.enablePlugins(PlayJava)
	
lazy val activity_api_service = (project in file("modules/activity_api_service"))
	.enablePlugins(PlayJava).dependsOn(activity_api, activitydb)	

// 调研模块 采集数据  
lazy val research = (project in file("modules/research"))
	.enablePlugins(PlayJava).dependsOn(base, activitydb)
	
// 忠诚度 Loyalty 与老客户互动
//lazy val Loyalty = (project in file("modules/Loyalty"))
//	.enablePlugins(PlayJava).dependsOn(base)
	
// 促销 promotion	
//lazy val promotion = (project in file("modules/promotion"))
//	.enablePlugins(PlayJava).dependsOn(base)
	

lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base, activitydb, activity_api, activity_api_service, research)
        .aggregate(base, activitydb, activity_api, activity_api_service, research)
        .settings(
                aggregate in update := false
        )

libraryDependencies ++= Seq(
  javaWs,
  filters,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
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