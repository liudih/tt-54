name := """dodocool"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

organization := "com.tomtop.dodocool"

lazy val base = (project in file("modules/base"))
	.enablePlugins(PlayJava)

		
lazy val member = (project in file("modules/member"))
	.enablePlugins(PlayJava)
	.dependsOn(base)

lazy val product = (project in file("modules/product"))
	.enablePlugins(PlayJava)
	.dependsOn(base)
	
lazy val interaction = (project in file("modules/interaction"))
	.enablePlugins(PlayJava)
	.dependsOn(product, member)

lazy val cart = (project in file("modules/cart"))
	.enablePlugins(PlayJava)
	.dependsOn(product, member)

lazy val google = (project in file("modules/google"))
	.enablePlugins(PlayJava)
	.dependsOn(member)
	
lazy val facebook = (project in file("modules/facebook"))
	.enablePlugins(PlayJava)
	.dependsOn(member)

lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base,member, product, interaction, cart, google,facebook)
        .aggregate(base,member, product, interaction, cart, google,facebook)
        .settings(
                aggregate in update := false
        )

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

javacOptions ++= Seq("-encoding", "UTF-8")

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

