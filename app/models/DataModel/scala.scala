package models.DataModel

import play.api.libs.json.OFormat


case class DataModel(_id: String,
                       name: String,
                       description: String,
                       pageCount: Int)
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
}

