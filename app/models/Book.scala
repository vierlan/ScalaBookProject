package models

import play.api.libs.json.{Json, OFormat}


case class Book(
                 name: String,
                 description: String) {

}

object Book {
  implicit val formats: OFormat[Book] = Json.format[Book]
}


//TERM pulls in ISBN


//}