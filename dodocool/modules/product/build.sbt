name := """product"""


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
  "org.elasticsearch" % "elasticsearch" % "1.4.2",
  "net.coobird" % "thumbnailator" % "0.4.8",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.0",
  "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.1.0",
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing(),
  "com.tomtop.website" % "product_api_2.11" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "interaction_api_2.11" % "1.0-SNAPSHOT"
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
