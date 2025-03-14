package controllers

import baseSpec.BaseSpecWithApplication
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component, repository, executionContext)

  "ApplicationController .index" should {

    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

  "ApplicationController .create()" should {
    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

  "ApplicationController .read()" should {
    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

  "ApplicationController .update()" should {
    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

  "ApplicationController .delete()" should {
    val result = TestApplicationController.index()(FakeRequest())

    "return TODO" in {
      status(result) shouldBe Status.NOT_IMPLEMENTED
    }
  }

}
