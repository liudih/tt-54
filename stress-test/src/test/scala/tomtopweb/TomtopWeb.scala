package tomtopweb

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TomtopWebSimulation extends Simulation {

	val server = if (System.getProperty("server") != null) {
		System.getProperty("server")
	} else {
		"http://192.168.7.13:8080"
	}

	val httpProtocol = http
		.baseURL(server)
		.acceptHeader("image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36")

        val scn = scenario("Frontend User")
                .exec(http("Home Page").get("/").check(status.is(200)))
		.exec(http("Keyword Search").get("/product?q=usb").check(status.is(200)))
		.exec(http("Category Browsing").get("/RC-Models-Hobbies").check(status.is(200)))
		.exec(http("New Arrivals").get("/product/newarrivals").check(status.is(200)))
		.exec(http("Shopping Cart").get("/cart").check(status.is(200)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
		.assertions(forAll.failedRequests.percent.lessThan(5))

}
