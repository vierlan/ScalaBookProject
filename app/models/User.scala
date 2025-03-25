package models

import play.api.libs.json.{Json, OFormat}

case class User(
                 _id: Option[Int],       // Option[Int] to handle auto-generated IDs
                 name: String
               )


object User {
  implicit val formats: OFormat[User] = Json.format[User]
}

