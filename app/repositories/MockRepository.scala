//package repositories
//
//import com.google.inject.ImplementedBy
//import models.{APIError, DataModel}
//import org.mongodb.scala.bson.conversions.Bson
//import org.mongodb.scala.model.Filters.empty
//import org.mongodb.scala.model.{Filters, IndexModel, Indexes, ReplaceOptions}
//import org.mongodb.scala.result
//import uk.gov.hmrc.mongo.MongoComponent
//import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
//
//import javax.inject.{Inject, Singleton}
//import scala.concurrent.{ExecutionContext, Future}
//
//
//trait MockRepository {
//
//  def index()(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]]
//
//  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]]
//
//  def readByName(name: String)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]
//
//  def read(id: String)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]
//
//  def update(id: String, book: DataModel)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, result.UpdateResult]]
//
//  def updateField(id: String, field: String, value: String)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, result.UpdateResult]]
//
//  def delete(id: String)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, result.DeleteResult]]
//
//  def readByISBN(isbn: String)(implicit ec: ExecutionContext): Future[Either[APIError.BadAPIResponse, Option[DataModel]]]
//
//}
//
//@Singleton
//class DataRepository @Inject()(
//                                mongoComponent: MongoComponent
//                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
//  collectionName = "dataModels",
//  mongoComponent = mongoComponent,
//  domainFormat = DataModel.formats,
//  indexes = Seq(IndexModel(
//    Indexes.ascending("_id")
//  )),
//  replaceIndexes = false
//) {
//
//  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
//    collection.find().toFuture().map {
//      case books: Seq[DataModel] => Right(books)
//      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
//    }
//
//  def create(book: DataModel): Future[DataModel] =
//    collection
//      .insertOne(book)
//      .toFuture()
//      .map(_ => book)
//
//  def byID(id: String): Bson =
//    Filters.and(
//      Filters.equal("_id", id)
//    )
//
//  def read(id: String): Future[Either[APIError.BadAPIResponse, DataModel]] =
//    collection.find(byID(id)).headOption.map {
//      case Some(data) => Right(data)
//      case _ => Left(APIError.BadAPIResponse(404, "Book cannot be found"))
//    }
//
//  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
//    collection.replaceOne(
//      filter = byID(id),
//      replacement = book,
//      options = new ReplaceOptions().upsert(true)
//    ).toFuture().map(result => {
//      Right(result)
//    }).recover {
//      case _ => Left(APIError.BadAPIResponse(404, "Book cannot be updated"))
//    }
//
//  def delete(id: String): Future[result.DeleteResult] =
//    collection.deleteOne(
//      filter = byID(id)
//    ).toFuture()
//
//  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests
//
//  def getData: String = "Data from repository"
//}