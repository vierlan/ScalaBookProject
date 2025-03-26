package models

import play.api.libs.json.{Json, OFormat}

case class VolumeInfo(
                     title: String,
                     pageCount: Option[Int],
                     description: Option[String],
                     thumbnailUrl: Map[String, String],
                     industryIdentifiers: Seq[IndustryIdentifier]
                     )


object VolumeInfo {
  implicit val formats: OFormat[VolumeInfo] = Json.format[VolumeInfo]
}