name := """mobile"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
	.enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
   "net.coobird" % "thumbnailator" % "0.4.8",
   "commons-collections" % "commons-collections" % "3.2.1",
   "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
   "com.tomtop.cart" % "cart_api_2.11" % "1.0-SNAPSHOT",
   "com.tomtop.website" % "base_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "product_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "member_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "order_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "interaction_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "messaging_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "paypal_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "loyalty_api_2.11" % "1.1-SNAPSHOT",
   "com.tomtop.website" % "advertising_api_2.11" % "1.1-SNAPSHOT"
 )

LessKeys.verbose in Assets := true

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

javacOptions ++= Seq("-encoding", "UTF-8")

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

