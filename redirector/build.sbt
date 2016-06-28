name := """redirector"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val common = (project in file("modules/common"))
	.enablePlugins(PlayJava)

lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(common)
        .aggregate(common)
        .settings(
                aggregate in update := false
        )
                


libraryDependencies ++= Seq(
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)

LessKeys.verbose in Assets := true

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

javacOptions ++= Seq("-encoding", "UTF-8")

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



