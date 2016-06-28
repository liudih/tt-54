name := """messaging_api"""

version := "1.1-SNAPSHOT"

organization := "com.tomtop.website"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

publishArtifact in (Compile, packageDoc) := false

sources in (Compile,doc) := Seq.empty

libraryDependencies ++= Seq(
  cache,
  javaWs
)

publishTo := {
  val repo = "http://192.168.220.54:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local")
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))

