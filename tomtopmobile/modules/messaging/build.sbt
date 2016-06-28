name := "messaging"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    javaWs,
    "com.tomtop.website" %% "common" % "1.0-SNAPSHOT",
    "com.tomtop.website" % "base_api_2.11" % "1.1-SNAPSHOT",
    "com.tomtop.website" % "messaging_api_2.11" % "1.1-SNAPSHOT",
    "com.tomtop.website" % "member_api_2.11" % "1.1-SNAPSHOT",
    "com.tomtop.website" % "product_api_2.11" % "1.1-SNAPSHOT"
)     

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
