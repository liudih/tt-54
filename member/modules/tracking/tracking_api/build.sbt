name := """tracking_api"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val tracking_api = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "com.tomtop.website" %% "base_api" % "1.0-SNAPSHOT"
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




