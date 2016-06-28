name := """testsupport"""

version := "1.0-SNAPSHOT"

lazy val testsupport = (project in file("."))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-test" % play.core.PlayVersion.current,
  "com.google.guava" % "guava" % "16.0.1",
  "com.tomtop.website" %% "common"  % "1.0-SNAPSHOT" changing()
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := true

publishTo := {
  val repo = "http://192.168.220.54:8080/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at repo + "libs-snapshot-local") 
  else
    Some("releases"  at repo + "libs-release-local")
}

credentials += Credentials(new File("artifactory/credentials"))