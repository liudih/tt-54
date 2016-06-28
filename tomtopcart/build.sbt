name := "tomtopcart"

organization := "com.tomtop.cart"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"	
	
lazy val base = (project in file("modules/base"))
	.enablePlugins(PlayJava)
	
lazy val cart_api = (project in file("modules/cart/cart_api"))
	.enablePlugins(PlayJava)
	
lazy val cart_services = (project in file("modules/cart/cart_services"))
	.enablePlugins(PlayJava)
	.dependsOn(cart_api,base)

lazy val cart = (project in file("modules/cart"))
    .dependsOn(cart_services)
	.enablePlugins(PlayJava)
	
lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base,cart,cart_api,cart_services)
        .aggregate(base,cart,cart_api,cart_services)
        .settings(
                aggregate in update := false
        )

libraryDependencies ++= Seq(
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.tomtop.website" % "base_api_2.11" % "1.1-SNAPSHOT",
  "com.alibaba" % "fastjson" % "1.2.6"
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

