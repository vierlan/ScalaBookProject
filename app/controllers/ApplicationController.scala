package controllers

import models.DataModel.DataModel
import org.mongodb.scala.result
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository
import repositories.DataRepository.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{Inject, Singleton}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository : DataRepository, implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
        dataRepository.read(id).map {
          case Right(data) => Ok(Json.toJson(data))
          case Left(_) => BadRequest
        }
    }

  def update(id: String): Action[JsValue] = Action.async (parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) => dataRepository.update(id, dataModel).map {
        case Right(_) =>
          Accepted(Json.toJson(dataModel))
        case Left(_) =>
          BadRequest
      }
      case JsError(_) => Future(BadRequest)
    }
    }


  def index(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.index().map{
      case Right(item: Seq[DataModel]) => if (item.length < 1 ) {
      BadRequest {Json.toJson(item)}}
      else
     {Ok {Json.toJson(item)}}
      case Left(_) => BadRequest(Json.toJson("Unable to find any books"))
    }
  }

//  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
//    dataRepository.read(id).map{
//      case dataModel => Ok {Json.toJson(dataModel)}
//      case _ => BadRequest("book id does not exist")
//    }
//  }

  def delete(id:String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>  if (dataModel._id == id) {
        dataRepository.create(dataModel).map(_ => Accepted)
      }
        else {
          Future(BadRequest)
        }
      case JsError(_) => Future(BadRequest)
    }
  }
//  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
//    dataRepository.delete(id).map{
//      case dataModel: DataModel =>
//    }
//    case _ => BadRequest
//  }
}
