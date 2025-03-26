package models

import play.api.libs.json.{Json, OFormat}

case class ImageLinks(thumbnail: String) {

}


object ImageLinks {
  implicit val formats: OFormat[ImageLinks] = Json.format[ImageLinks]
}