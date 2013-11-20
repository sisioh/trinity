package org.sisioh.trinity.test

import org.sisioh.trinity.domain.io.http.Method._
import org.sisioh.trinity.domain.mvc.action.SimpleAction
import org.sisioh.trinity.domain.mvc.http.Response
import org.sisioh.trinity.domain.mvc.routing.RouteDsl._
import org.sisioh.trinity.domain.mvc.routing.RoutingFilter
import org.sisioh.trinity.domain.mvc.server.Server
import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ControllerIntegrationTestSupportSpec extends Specification with ControllerIntegrationTestSupport {

  sequential

  def helloWorld = SimpleAction {
    request =>
      Future.successful(Response().withContentAsString("Hello World!"))
  }

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld
      )
  }

  "integration-test" should {
    "test get method" in new WithServer(Server(filter = Some(routingFilter))) {
      testGet("/hello") {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "Hello World!"
          }
      }
    }
    "test get method without WithServer Scope" in {
      val server = Server(filter = Some(routingFilter))
      val f = server.start().map {
        _ =>
          testGet("/hello") {
            result =>
              result must beSuccessfulTry.like {
                case response =>
                  response.contentAsString() must_== "Hello World!"
              }
          }
      }.flatMap {
        result =>
          server.stop.map(_ => result)
      }
      Await.result(f, Duration.Inf)
    }

  }
}
