package models

import play.api.libs.json.{Json, OFormat}

case class GoogleBook(
                     id: String,
                     volumeInfo: VolumeInfo
                     )


object GoogleBook {
  implicit val formats: OFormat[GoogleBook] = Json.format[GoogleBook]
}

case class ResultList(items: Seq[GoogleBook])

object ResultList {
  implicit val formats: OFormat[ResultList] = Json.format[ResultList]
}