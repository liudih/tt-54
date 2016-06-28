name := """product_api"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val product_api = (project in file("."))

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.freemarker" % "freemarker" % "2.3.21",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.elasticsearch" % "elasticsearch" % "1.6.0",
  "net.coobird" % "thumbnailator" % "0.4.8",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.0",
  "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.1.0",
  "com.tomtop.website" %% "base_api" % "1.0-SNAPSHOT",
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

