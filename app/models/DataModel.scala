package models

import play.api.data.Form
import play.api.data.Forms.{mapping, number, optional, text}
import play.api.libs.json.{Json, OFormat}


case class DataModel(_id: String,
                       name: String,
                       description: String,
//                     thumbnailUrl: String,
                       pageCount: Int,
                    )
/**
let books =
[
{
  "ID" : "1000",
  "name" : "The Philosophers Stone",
  "description" : "Magical",
  "pageCount" : 5000,
},
{
"ID" : "5000",
"name" : "His Dark Materials",
"description" : "Mythical",
"pageCount" : 3000,
}
]
*/


object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
  def toBook(dataModel: DataModel): Book = {
    Book(
      isbn = dataModel._id,
      title = dataModel.name,
      description = dataModel.description,
      pageCount = dataModel.pageCount
    )
  }
//  val dataModelForms: Form[DataModel] = Form(
//    mapping(
//      "_id" -> text,
//      "title" -> text,
//      "description" -> optional(text),
//      "pageCount" -> optional(number)
//    )(DataModel.apply())(DataModel.unapply)
//
//  )
}

