package models

import play.api.libs.json.{Json, OFormat}

case class GoogleBooksResponse(
                              kind: String,
                              totalItems: Int,
                              items: Seq[GoogleBook]
                              )


object GoogleBooksResponse {
  implicit val formats: OFormat[GoogleBooksResponse] = Json.format[GoogleBooksResponse]
}