name := "cart_api"

organization := "com.tomtop.cart"

version := "1.0-SNAPSHOT"

lazy val cart_api = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  	javaWs,
    "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
    "com.alibaba" % "fastjson" % "1.2.6",
  	"com.tomtop.website" % "order_api_2.11" % "1.1-SNAPSHOT"
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
