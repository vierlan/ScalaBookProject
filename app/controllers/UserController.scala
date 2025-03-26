package controllers

import models.{APIError, User}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.UserRepository
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

  def login: Action[AnyContent] = Action {
    Ok(views.html.login())
  }

  def validateLogin: Action[AnyContent] = Action { request =>
    val loginVals = request.body.asFormUrlEncoded
    Ok(views.html.userPage())
  }

  def createUser(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[User].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid JSON"))),
      user => {
        userRepository.create(user).map(createdUser => Created(Json.toJson(createdUser)))
      }
    )
  }

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    userRepository.read(id).map {
      case Right(data) => Ok(Json.toJson(data))
      case Left(_) => BadRequest("Bad request")
    }
  }

  def update(id: String): Action[JsValue] = Action.async (parse.json) { implicit request =>
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

  def delete(id: String): Action[AnyContent] = Action.async {
    userRepository.delete(id).map {
      case result if result.getDeletedCount > 0 => Accepted
      case _ => NotFound(s"User with ID $id not found")
    }
  }
}
