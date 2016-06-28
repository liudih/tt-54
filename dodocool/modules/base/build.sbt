name := """base"""

version := "1.0-SNAPSHOT"

lazy val base = (project in file("."))
	.enablePlugins(PlayJava)

	
scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
  "com.maxmind.geoip2" % "geoip2" % "2.1.0",
  "com.github.napp-com" % "kaptcha" % "1.0.0",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "commons-io" % "commons-io" % "2.2",
  "com.memetix" % "microsoft-translator-java-api" % "0.6.2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "less" % "2.1.0",
  "org.webjars" % "jquery-ui" % "1.11.2",
  "org.webjars" % "jquery-validation" % "1.13.1",
  "org.webjars" % "jquery" % "1.11.2" force(),
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing(),
  "org.apache.commons" % "commons-email" % "1.3.3",
  "com.typesafe.play" % "play-mailer_2.11" % "3.0.0-M1",
  "javax.mail" % "mail" % "1.5.0-b01",
  "javax.activation" % "activation" % "1.1.1",
  "org.apache.poi" % "poi-ooxml" % "3.12-beta1",
  "com.tomtop.website" % "base_api_2.11" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "advertising_api_2.11" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "member_api_2.11" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "order_api_2.11" % "1.0-SNAPSHOT"
)

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false


