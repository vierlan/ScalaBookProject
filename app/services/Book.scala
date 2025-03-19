package services

import models.DataModel
import play.api.libs.json.{Json, OFormat}


case class Book(
                 authors: List[String],
                 title: String) {

}

object Book {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}


//TERM pulls in ISBN


//}