package controllers

import models.{APIError, User}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.UserRepository
import services.UserService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Singleton

@Singleton
class UserController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       val userRepository : UserRepository,
                                       val userService: UserService,
                                       implicit val ec: ExecutionContext) extends BaseController {

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(user, _) =>
        userRepository.create(user).map(_ => Created)
      case JsError(_) => Future(BadRequest("Denied in controller"))
    }
  }

  def login(): Action[AnyContent] = Action {
    Ok(views.html.login())
  }

  def validateLogin: Action[AnyContent] = Action.async (parse.anyContent) { request =>
    val loginVals = request.body.asFormUrlEncoded

    // Extract login credentials (e.g., username and password)
    loginVals match {
      case Some(values) =>
        val usernameOpt = values.get("username").flatMap(_.headOption)
        val passwordOpt = values.get("password").flatMap(_.headOption)

        (usernameOpt, passwordOpt) match {
          case (Some(username), Some(password)) =>
            // Validate the user (this could involve querying the repository)
            userRepository.validateCredentials(username, password).map {
              case Some(user) =>
                // Redirect to the userPage with the user object
                Redirect(routes.UserController.userPage()).flashing(
                  "userID" -> user._id, // You could pass the user ID or any other user data
                  "username" -> user.name
                )
              case None =>
                // Invalid login attempt
                Redirect(routes.UserController.login.flashing(
                  "error" -> "Invalid username or password"
                )
            }
          case _ =>
            // Missing credentials
            Future.successful(
              Redirect(routes.UserController.login()).flashing(
                "error" -> "Please provide both username and password"
              )
            )
        }
      case None =>
        // Invalid request payload
        Future.successful(
          Redirect(routes.UserController.login()).flashing(
            "error" -> "Invalid login request"
          )
        )
    }
  }


  def userPage(): Action[AnyContent] = Action { request =>
    // Extract flashed data (userID, username, etc.)
    val userID = request.flash.get("userID")
    val username = request.flash.get("username")

    userID match {
      case Some(id) =>
        // Render the userPage with the user details
        Ok(views.html.userPage(id, username.getOrElse("Unknown User")))
      case None =>
        // If user data is missing, redirect back to login
        Redirect(routes.UserController.login()).flashing(
          "error" -> "User information missing"
        )
    }
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
