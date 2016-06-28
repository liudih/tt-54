name := """tomtopweb"""

organization := "com.tomtop.website"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val testsupport = project
	
lazy val base_services = (project in file("modules/base/base_services"))
	.enablePlugins(PlayJava)
	.dependsOn(testsupport % "test")

lazy val base = (project in file("modules/base"))
	.enablePlugins(PlayJava)
	.dependsOn(base_services)
	.dependsOn(testsupport % "test")

lazy val member_services = (project in file("modules/member/member_services"))
	.enablePlugins(PlayJava)
	.dependsOn(base_services, email)

lazy val member = (project in file("modules/member"))
	.enablePlugins(PlayJava)
	.dependsOn(base,member_services)
	.dependsOn(testsupport % "test")

lazy val product_services = (project in file("modules/product/product_services"))
	.enablePlugins(PlayJava)
	.dependsOn(base_services)

lazy val product = (project in file("modules/product"))
	.enablePlugins(PlayJava)
	.dependsOn(base,product_services)
	.dependsOn(testsupport % "test")

lazy val order_services = (project in file("modules/order_services"))
	.enablePlugins(PlayJava)
	.dependsOn(member_services, product_services, email)

lazy val order = (project in file("modules/order"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product, order_services)
	.dependsOn(testsupport % "test")

lazy val interaction_services = (project in file("modules/interaction_services"))
	.enablePlugins(PlayJava)
	.dependsOn(base_services, product_services, member_services, order, email)

lazy val interaction = (project in file("modules/interaction"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product, order, livechat, loyalty, interaction_services)
	.dependsOn(testsupport % "test")

lazy val loyalty_services = (project in file("modules/loyalty_services"))
	.enablePlugins(PlayJava)
	.dependsOn(member_services, product_services, order_services, email)

lazy val loyalty = (project in file("modules/loyalty"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product, order, loyalty_services)
	.dependsOn(testsupport % "test")

lazy val advertising_services = (project in file("modules/advertising_services"))
	.enablePlugins(PlayJava)
	.dependsOn(product_services)

lazy val advertising = (project in file("modules/advertising"))
	.enablePlugins(PlayJava)
	.dependsOn(product,advertising_services)
	.dependsOn(testsupport % "test")

lazy val payment = (project in file("modules/payment"))
	.enablePlugins(PlayJava)
	.dependsOn(order)
	.dependsOn(testsupport % "test")

lazy val facebook = (project in file("modules/facebook"))
	.enablePlugins(PlayJava)
	.dependsOn(member, interaction)
	.dependsOn(testsupport % "test")

lazy val paypal = (project in file("modules/paypal"))
	.enablePlugins(PlayJava)
	.dependsOn(member, order)
	.dependsOn(testsupport % "test")

lazy val tracking = (project in file("modules/tracking"))
	.enablePlugins(PlayJava)	
	.dependsOn(base, order, member)

lazy val google = (project in file("modules/google"))
	.enablePlugins(PlayJava)
	.dependsOn(member, interaction)
	.dependsOn(testsupport % "test")

lazy val messaging_services = (project in file("modules/messaging/messaging_services"))
	.enablePlugins(PlayJava)
	.dependsOn(member_services)
	.dependsOn(testsupport % "test")

lazy val messaging = (project in file("modules/messaging"))
	.enablePlugins(PlayJava)
	.dependsOn(member, product,messaging_services)
	.dependsOn(testsupport % "test")

lazy val livechat = (project in file("modules/livechat"))
	.enablePlugins(PlayJava)
	.dependsOn(base)
	.dependsOn(testsupport % "test")

lazy val wholesale = (project in file("modules/wholesale"))
    .enablePlugins(PlayJava)
	.dependsOn(member, product, order)
	.dependsOn(testsupport % "test")

lazy val shareasale = (project in file("modules/shareasale"))
	.enablePlugins(PlayJava)
	.dependsOn(order)
	.dependsOn(testsupport % "test")

lazy val twitter = (project in file("modules/twitter"))
	.enablePlugins(PlayJava)
	.dependsOn(base, member, interaction)
	.dependsOn(testsupport % "test")

lazy val vizury = (project in file("modules/vizury"))
	.enablePlugins(PlayJava)
	.dependsOn(base)

lazy val email = (project in file("modules/email"))
	.enablePlugins(PlayJava)
	
lazy val root = (project in file("."))
        .enablePlugins(PlayJava)
        .dependsOn(member, product, base, order, interaction, loyalty, advertising,payment,
		facebook, paypal, tracking,google, shareasale, messaging, twitter, livechat,base_services, wholesale,product_services,
		member_services,interaction_services, advertising_services, loyalty_services, order_services
		,messaging_services,email)
        .aggregate(member, product, base, order, interaction, loyalty, advertising, payment,
		facebook, paypal, tracking,google, shareasale, messaging, twitter, livechat,base_services, wholesale,product_services,
		 member_services,interaction_services,advertising_services, loyalty_services,order_services
		,messaging_services,email)
        .settings(
                aggregate in update := false
        )

libraryDependencies ++= Seq(
  javaWs,
  filters,
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
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

