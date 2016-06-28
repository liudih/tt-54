name := """product"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaWs,
  "com.tomtop.website" % "product_api_2.11" % "1.1-SNAPSHOT",
  "com.tomtop.website" % "advertising_api_2.11" % "1.1-SNAPSHOT",
  "com.tomtop.website" % "base_api_2.11" % "1.1-SNAPSHOT",
  "com.alibaba" % "fastjson" % "1.2.6",
  "org.freemarker" % "freemarker" % "2.3.21",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.elasticsearch" % "elasticsearch" % "1.4.2",
  "net.coobird" % "thumbnailator" % "0.4.8",
  "com.twelvemonkeys.imageio" % "imageio-core" % "3.1.0",
  "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.1.0",
  "com.google.http-client" % "google-http-client" % "1.19.0"
)


