
name := """tomtopapi"""

version := "1.1-SNAPSHOT"

scalaVersion := "2.11.1"

organization := "com.tomtop.website"

lazy val base_api = (project in file("modules/base_api"))
	.enablePlugins(PlayJava)
	
lazy val member_api = (project in file("modules/member_api"))
	.enablePlugins(PlayJava)
	.dependsOn(base_api)

lazy val product_api = (project in file("modules/product_api"))
	.enablePlugins(PlayJava)
	.dependsOn(base_api)

lazy val order_api = (project in file("modules/order_api"))
	.enablePlugins(PlayJava)
	.dependsOn(member_api, product_api, base_api)
	
lazy val interaction_api = (project in file("modules/interaction_api"))
	.enablePlugins(PlayJava)
	.dependsOn(product_api, member_api, order_api, base_api)

lazy val loyalty_api = (project in file("modules/loyalty_api"))
	.enablePlugins(PlayJava)
	.dependsOn(member_api, product_api, order_api, base_api)
	

lazy val advertising_api = (project in file("modules/advertising_api"))
	.dependsOn(product_api, base_api)
	.enablePlugins(PlayJava)
	

lazy val facebook_api = (project in file("modules/facebook_api"))
	.enablePlugins(PlayJava)
	.dependsOn(member_api)


lazy val paypal_api = (project in file("modules/paypal_api"))
	.enablePlugins(PlayJava)
	.dependsOn(member_api)


lazy val tracking_api = (project in file("modules/tracking_api"))
	.enablePlugins(PlayJava)
	.dependsOn(base_api)
	

lazy val google_api = (project in file("modules/google_api"))
	.enablePlugins(PlayJava)
	.dependsOn(member_api)
	

lazy val messaging_api = (project in file("modules/messaging_api"))
	.enablePlugins(PlayJava)
	.dependsOn(base_api)
	
	
lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(base_api,tracking_api,product_api,member_api, interaction_api, advertising_api, loyalty_api,order_api,messaging_api,facebook_api,google_api,paypal_api)
        .aggregate(base_api,tracking_api,product_api,member_api, interaction_api, advertising_api, loyalty_api,order_api,messaging_api,facebook_api,google_api,paypal_api)
        .settings(
                aggregate in update := false
        )
       
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters
)

javacOptions ++= Seq("-encoding", "UTF-8")

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

