name := """facebook"""

version := "1.0-SNAPSHOT"

lazy val facebook = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"



	
libraryDependencies ++= Seq(
  cache,
  javaWs,
  "com.tomtop.website" % "data-transfer-object" % "0.0.1-SNAPSHOT" changing(),
  "com.tomtop.website" % "member_api_2.11" % "1.0-SNAPSHOT"
)


        
sources in (Compile,doc) := Seq.empty