package controllers

import models.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


class ApplicationController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       val dataRepository: DataRepository,
                                       implicit val ec: ExecutionContext)
  extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => Ok {Json.toJson(item)}
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(id: String): Action[AnyContent] = Action.async {
    dataRepository.read(id).map { book =>
      Ok(Json.toJson(book))
    }.recover {
      case _: NoSuchElementException => NotFound(Json.toJson(s"Book with id '$id' not found"))
      case ex: Exception => InternalServerError(Json.toJson(s"An error occurred: ${ex.getMessage}"))
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.update(id, dataModel).flatMap {
        _ => dataRepository.read(id).map(book => Accepted (Json.toJson(book)))
        }
      case JsError(_) => Future(BadRequest)
    }}

  def delete(id: String): Action[AnyContent] = Action.async {
    dataRepository.delete(id).map { _ =>
      Accepted
    }.recover {
      case _: NoSuchElementException => NotFound(Json.toJson(s"Book with title '$id' not found"))
      case ex: Exception => InternalServerError(Json.toJson(s"An error occurred: ${ex.getMessage}"))
    }
  }


}
