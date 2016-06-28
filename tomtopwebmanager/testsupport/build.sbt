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

publishArtifact in (Compile, packageDoc) := false

