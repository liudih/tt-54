
name := """tomtopemail"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

organization := "com.tomtop.email"

	
lazy val email_api = (project in file("modules/email_api"))
	.enablePlugins(PlayJava)
	
lazy val email = (project in file("modules/email"))
	.enablePlugins(PlayJava)
	.dependsOn(email_api)
	
lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(email_api,email)
        .aggregate(email_api,email)
        .settings(
                aggregate in update := false
        )
       
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "commons-discovery" % "commons-discovery" % "0.5",
  "axis" % "axis-wsdl4j" % "1.5.1",
  filters
)

javacOptions ++= Seq("-encoding", "UTF-8")

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

