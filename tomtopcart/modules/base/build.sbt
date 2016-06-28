name := "base"

organization := "com.tomtop.cart"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaWs,
  "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
  "com.tomtop.website" % "base_api_2.11" % "1.1-SNAPSHOT",
  "commons-beanutils" % "commons-beanutils" % "1.9.2",
  "commons-io" % "commons-io" % "2.2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "less" % "2.1.0",
  "org.webjars" % "jquery-ui" % "1.11.2",
  "org.webjars" % "jquery-validation" % "1.13.1",
  "org.webjars" % "jquery" % "1.11.2" force(),
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing(),
  "com.typesafe.play" % "play-mailer_2.11" % "3.0.0-M1",
  "javax.activation" % "activation" % "1.1.1",
  "com.google.guava" % "guava" % "16.0.1",
  "com.tomtop.website" % "member_api_2.11" % "1.1-SNAPSHOT"
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
