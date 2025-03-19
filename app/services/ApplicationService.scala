package services

import connectors.LibraryConnector
import models.DataModel
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Singleton


@Singleton
class ApplicationService @Inject()(connector: LibraryConnector) {

  def getGoogleBook(urlOverride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): Future[DataModel] =
    connector.get[DataModel](urlOverride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term"))

}


