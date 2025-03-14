package controllers

import models.DataModel.DataModel
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository.repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository : DataRepository, implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async (parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.update(id, dataModel).flatMap {
        _=> dataRepository.read(id).map(book => Accepted {Json.toJson(book)})
      }
      case JsError(_) => Future(BadRequest)
    }
    }


  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => Ok {Json.toJson(item)}
      case Left(error) => Status(error)(Json.toJson("Unable to find any books"))
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.read(id).map{
      dataModel => Ok {Json.toJson(dataModel)}
      //case Left(error) => Status(error)(Json.toJson("Your book is book")
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id).map{
      dataModel => Accepted
      //case Left(error) => Status(error)(Json.toJson("Your book is book")
    }
  }


}
