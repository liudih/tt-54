name := """base"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val base_api = (project in file("base_api"))
        .enablePlugins(PlayJava)

lazy val base_services = (project in file("base_services"))
        .enablePlugins(PlayJava)
        .dependsOn(base_api)

lazy val base = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base_services)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
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
  "org.apache.poi" % "poi-ooxml" % "3.12-beta1"
)

LessKeys.verbose in Assets := true

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

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

