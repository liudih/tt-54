name := """base"""

version := "1.0-SNAPSHOT"

organization := "com.tomtop.website.activity"

scalaVersion := "2.11.1"

lazy val base = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaWs,
  filters,
  "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "base_api_2.11" % "1.0-SNAPSHOT",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "commons-io" % "commons-io" % "2.2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "less" % "2.1.0",
  "org.webjars" % "jquery-ui" % "1.11.2",
  "org.webjars" % "jquery-validation" % "1.13.1",
  "org.webjars" % "jquery" % "1.11.2" force(),
  "javax.activation" % "activation" % "1.1.1",
  "com.google.guava" % "guava" % "16.0.1",
  "com.tomtop.website" %% "member_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "product_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "messaging_api" % "1.1-SNAPSHOT",
  "com.tomtop.website" %% "interaction_api" % "1.1-SNAPSHOT"
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

sources in (Compile,doc) := Seq.empty

publishTo := {
  val repo = "http://192.168.7.15:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local")
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))
