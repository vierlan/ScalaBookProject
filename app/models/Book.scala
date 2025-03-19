package models

import play.api.libs.json.{Json, OFormat}


case class Book(
                 authors: List[String],
                 title: String) {

}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}


//TERM pulls in ISBN


//}