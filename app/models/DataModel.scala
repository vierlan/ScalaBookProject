package models
import play.api.data._
import play.api.data.Forms._

import play.api.data.Form
import play.api.data.Forms.{mapping, number, optional, text}
import play.api.libs.json.{Json, OFormat}


case class DataModel(_id: String,
                       title: String,
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

//  case class Person(name: String, surname: String, age: Int)
//  object Person {
//    implicit val formats: OFormat[Person] = Json.format[Person]
//
//    val personForm: Form[Person] = Form(
//      mapping(
//        "name" -> text,
//        "surname" -> text,
//        "age" -> number
//      )(Person.apply)(Person.unapply)
//    )
//  }

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
