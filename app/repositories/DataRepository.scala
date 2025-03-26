package repositories

import com.google.inject.ImplementedBy
import models.{APIError, DataModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import play.api.libs.json.JsError
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[DataRepository])
trait DataRepositoryTrait {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]]

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]]

//  def readByName(name: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]

  def read(id: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]]

  //def updateField(id: String, field: String, value: String): Future[Either[APIError.BadAPIResponse, result.UpdateResult]]

  def delete(id: String): Future[Either[APIError.BadAPIResponse, result.DeleteResult]]

  //def readByISBN(isbn: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]

}



@Singleton
class DataRepository @Inject() (
                                mongoComponent: MongoComponent
                              ) (implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel] (
  collectionName = "dataModels",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(Indexes.ascending("_id"))),
  replaceIndexes = false
) with DataRepositoryTrait {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]]  =
    collection.find().toFuture().map{
      case books: Seq[DataModel] => Right(books)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    collection
      .insertOne(book)
      .toFuture()
      .map(_ => Right(book))

  def byID(id: String): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] =
    collection.find(byID(id)).headOption.map {
      case Some(data) => Right(Some(data))
      case _ => Left(APIError.BadAPIResponse(404, "Book cannot be found"))
    }

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true)
    ).toFuture().map(result => {
      Right(result)}).recover {
      case _ => Left(APIError.BadAPIResponse(404, "Book cannot be updated"))
    }

  def delete(id: String): Future[Either[APIError.BadAPIResponse,result.DeleteResult]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFuture().map(Right(_)) // Do validation here instead of this.

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests

}
