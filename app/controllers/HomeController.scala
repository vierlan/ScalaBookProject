package controllers


import models.Book
import play.api.libs.json.{JsArray, JsValue}
import play.api.libs.ws.WSClient
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                                val controllerComponents: ControllerComponents,
                                ws: WSClient
                              )(implicit ec: ExecutionContext) extends BaseController {
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok(views.html.index()))
  }

  def search(): Action[AnyContent] = Action.async { implicit request =>
    val query = request.getQueryString("query").getOrElse("")

    if (query.isEmpty) {
      Future.successful(Redirect(routes.HomeController.index()))
    } else {
      val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
      val url = s"https://www.googleapis.com/books/v1/volumes?q=$encodedQuery"

      ws.url(url).get().map { response =>
        val books = parseGoogleBooksResponse(response.json)
        Ok(views.html.searchResults(books))
      }
    }
  }

  private def parseGoogleBooksResponse(json: JsValue): Seq[Book] = {
    val items = (json \ "items").asOpt[JsArray].getOrElse(JsArray())

    items.value.flatMap { item =>
      val volumeInfo = (item \ "volumeInfo")

      for {
        title <- (volumeInfo \ "title").asOpt[String]
        // Use a default ISBN if not available
        isbn = (volumeInfo \ "industryIdentifiers").asOpt[JsArray]
          .flatMap(_.value.headOption)
          .flatMap(id => (id \ "identifier").asOpt[String])
          .getOrElse("Unknown")
        // Use default values for optional fields
        pageCount = (volumeInfo \ "pageCount").asOpt[Int].getOrElse(0)
        description = (volumeInfo \ "description").asOpt[String].getOrElse("No description available")
        thumbnailUrl = (volumeInfo \ "imageLinks" \ "thumbnail").asOpt[String]
          .getOrElse("/assets/images/no-cover.jpg")
      } yield Book(
        isbn = isbn,
        title = title,
        pageCount = pageCount,
        thumbnailUrl = thumbnailUrl,
        description = description
      )
    }.toSeq
  }
}
