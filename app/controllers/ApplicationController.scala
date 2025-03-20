package controllers

import models.DataModel
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.DataRepository
import services.LibraryService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Singleton

@Singleton
class ApplicationController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       val dataRepository : DataRepository,
                                       val service : LibraryService,
                                       implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
        dataRepository.read(id).map {
          case Right(data) => Ok(Json.toJson(data))
          case Left(_) => BadRequest("Bad request")
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
      BadRequest}
      else
     {Ok {Json.toJson(item)}}
      case Left(_) => BadRequest(Json.toJson("Unable to find any books"))
    }
  }

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

  def getGoogleBook(search: String, term: String): Action[AnyContent] = Action.async { implicit request =>
    println("request")
    service.getGoogleBook(search = search, term = term).map  { book => Ok (Json.toJson(book))
    }.recover {
      case e: Exception => InternalServerError(Json.obj("error" -> e.getMessage))
    }
  }
}
