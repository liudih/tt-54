name := """tomtopmobile"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val base = (project in file("modules/base"))
	.enablePlugins(PlayJava)
	
lazy val product = (project in file("modules/product"))
    .dependsOn(base)
	.enablePlugins(PlayJava)
	
lazy val common = (project in file("modules/common"))
	.enablePlugins(PlayJava)
	
lazy val member = (project in file("modules/member"))
	.enablePlugins(PlayJava)
	.dependsOn(base,product,common)
	
lazy val order = (project in file("modules/order"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product)
	
lazy val facebook = (project in file("modules/facebook"))
	.enablePlugins(PlayJava)
	.dependsOn(member)
	
lazy val google = (project in file("modules/google"))
	.enablePlugins(PlayJava)
	.dependsOn(member)
	
lazy val paypal = (project in file("modules/paypal"))
	.enablePlugins(PlayJava)
	.dependsOn(member,order)
	
lazy val messaging = (project in file("modules/messaging"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product)
	
lazy val interaction = (project in file("modules/interaction"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product, order)
	
lazy val tracking = (project in file("modules/tracking"))
	.enablePlugins(PlayJava)
	.dependsOn(member)
	
	
lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base,product,common,member,order,interaction,facebook,messaging,google,paypal,tracking)
        .aggregate(base,product,common,member,order,interaction,facebook,messaging,google,paypal,tracking)
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

