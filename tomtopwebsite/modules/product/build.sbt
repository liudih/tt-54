name := """product"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val product = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.freemarker" % "freemarker" % "2.3.21",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.elasticsearch" % "elasticsearch" % "1.5.2",
  "net.coobird" % "thumbnailator" % "0.4.8",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.0",
  "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.1.0",
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing(),
   "com.alibaba" % "fastjson" % "1.2.7",
  "org.csource" % "fastdfs_client" % "1.24"
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

