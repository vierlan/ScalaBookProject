package models

import play.api.libs.json.{Json, OFormat}

case class Thumbnail(
                      thumbnail: String
                    )


object Thumbnail {
  implicit val formats: OFormat[IndustryIdentifier] = Json.format[IndustryIdentifier]
}
