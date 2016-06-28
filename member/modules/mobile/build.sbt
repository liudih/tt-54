name := "mobile"

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
	.enablePlugins(PlayJava)

scalaVersion := "2.11.1"
	
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "commons-codec" % "commons-codec" % "1.10",
  "com.tomtop.website" %% "product_api" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "loyalty_api" % "1.0-SNAPSHOT",
  "com.tomtop.website" %% "advertising_api" % "1.0-SNAPSHOT"
)

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

