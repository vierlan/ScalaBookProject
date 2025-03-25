package controllers

import akka.util.Helpers.Requiring
import models.{APIError, User}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.UserRepository
import services.LibraryService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Singleton

@Singleton
class UserController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       val userRepository : UserRepository,
                                       implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(user, _) =>
        userRepository.create(user).map(_ => Created)
      case JsError(_) => Future(BadRequest("Denied in controller"))
    }
  }

  def read(id: Int): Action[AnyContent] = Action.async { implicit request =>
    userRepository.read(id).map {
      case Right(data) => Ok(Json.toJson(data))
      case Left(_) => BadRequest("Bad request")
    }
  }

  def update(id: Int): Action[JsValue] = Action.async (parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(user, _) => userRepository.update(id, user).map {
        case Right(_) =>
          Accepted(Json.toJson(user))
        case Left(_) =>
          BadRequest("Cannot update")
      }
      case JsError(_) => Future(BadRequest("Cannot update"))
    }
  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    userRepository.index().map{
      case Right(item: Seq[User]) => if (item.length < 1 ) {
        BadRequest("Unable to find any books")}
      else
      {Ok {Json.toJson(item)}}
      case Left(_) => BadRequest(Json.toJson("Unable to find any books"))
    }
  }

  def delete(id: Int): Action[AnyContent] = Action.async {
    userRepository.delete(id).map {
      case result if result.getDeletedCount > 0 => Accepted
      case _ => NotFound(s"User with ID $id not found")
    }
  }

//  def delete(id: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>
//    request.body.validate[User] match {
//      case JsSuccess(user, _) => {
//        if (user._id.contains(id)) {
//          userRepository.delete(((user._id))).map(_ => Accepted)
//        }
//        else {
//          Future(BadRequest("cannot delete"))
//        }
//      }
//      case JsError(_) => Future(BadRequest("cannot delete"))
//    }
//  }
}
