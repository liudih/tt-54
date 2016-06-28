name := """common"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

lazy val common = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.mybatis" % "mybatis" % "3.2.8",
  "org.mybatis" % "mybatis-guice" % "3.6",
  "org.redisson" % "redisson" % "1.2.1",
  "org.apache.camel" % "camel-core" % "2.14.1",
  "org.apache.camel" % "camel-quartz2" % "2.14.1",
  "com.caucho" % "hessian" % "4.0.38",
  "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.3",
  "com.tomtop" % "framework" % "0.0.1-SNAPSHOT" changing(),
  "com.tomtop" % "mybatis" % "0.0.1-SNAPSHOT" changing(),
  "org.apache.poi" % "poi-ooxml" % "3.12-beta1",
  "commons-io" % "commons-io" % "2.2",
  "org.liquibase" % "liquibase-core" % "3.3.0" exclude("org.eclipse.jetty", "jetty-servlet") exclude("junit", "junit")
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
