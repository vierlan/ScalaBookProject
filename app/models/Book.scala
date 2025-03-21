package models

import play.api.libs.json.{Json, OFormat}


case class Book(
                  isbn: String,
                 title: String,
                  pageCount: Int,
                 description: String) {

}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}


//TERM pulls in ISBN


//}