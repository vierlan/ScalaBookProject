package controllers

import baseSpec.BaseSpecWithApplication
import models.DataModel.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers._
import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component, repository, executionContext)

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  "ApplicationController .create" should {

    "create a book in the database" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED
      afterEach()
    }

  }
  override def beforeEach(): Unit = await(repository.deleteAll())
  override def afterEach(): Unit = await(repository.deleteAll())

//  "ApplicationController .index" should {
//
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return TODO" in {
//      status(result) shouldBe Status.NOT_IMPLEMENTED
//    }
//  }
//
//  "ApplicationController .create()" should {
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return TODO" in {
//      status(result) shouldBe Status.NOT_IMPLEMENTED
//    }
//  }
//
//  "ApplicationController .read()" should {
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return TODO" in {
//      status(result) shouldBe Status.NOT_IMPLEMENTED
//    }
//  }
//
//  "ApplicationController .update()" should {
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return TODO" in {
//      status(result) shouldBe Status.NOT_IMPLEMENTED
//    }
//  }
//
//  "ApplicationController .delete()" should {
//    val result = TestApplicationController.index()(FakeRequest())
//
//    "return TODO" in {
//      status(result) shouldBe Status.NOT_IMPLEMENTED
//    }
//  }

}
