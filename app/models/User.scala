package models

import play.api.libs.json.{Json, OFormat}

case class User(
                 id: Option[Int],       // Option[Int] to handle auto-generated IDs
                 name: String,
                 email: String,
                 age: Int
               )


object User {
  implicit val formats: OFormat[User] = Json.format[User]
}

