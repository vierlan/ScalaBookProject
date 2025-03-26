package connectors

import models.APIError
import play.api.libs.json.OFormat
import play.api.libs.json.{JsError, JsResult, JsSuccess, Json, OFormat}
import play.api.libs.ws.{WSClient, WSResponse}
import cats.data.EitherT
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


class LibraryConnector @Inject()(ws: WSClient) {
  def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response
        .map {
          result =>
            result.json.validate[Response] match {
              case JsSuccess(bookList, _) => Right(bookList)
              case JsError(errors) => Left(APIError.BadAPIResponse(500, s"Could not connect: $errors"))
            }
        }
    }
  }
}