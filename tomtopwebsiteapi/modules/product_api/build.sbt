name := """product_api"""

organization := "com.tomtop.website"

version := "1.1-SNAPSHOT"

lazy val product_api = (project in file("."))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.elasticsearch" % "elasticsearch" % "1.5.2",
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing()
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

