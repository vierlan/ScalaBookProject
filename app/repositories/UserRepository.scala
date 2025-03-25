package repositories

import models.{APIError, User}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.result
import play.api.libs.json.JsError
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[User](
  collectionName = "users",
  mongoComponent = mongoComponent,
  domainFormat = User.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("_id")
  )),
  replaceIndexes = false
) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[User]]]  =
    collection.find().toFuture().map{
      case user => Right(user)
      case _ => Left(APIError.BadAPIResponse(404, "User cannot be found"))
    }

  def create(user: User): Future[User] =
    collection
      .insertOne(user)
      .toFuture()
      .map(_ => user)

  private def byID(id: Int): Bson =
    Filters.and(
      Filters.equal("_id", id)
    )

  def read(id: Int): Future[Either[APIError.BadAPIResponse, User]] =
    collection.find(byID(id)).headOption.map {
      case Some(data) => Right(data)
      case _ => Left(APIError.BadAPIResponse(404, "Book cannot be found"))
    }

  def update(id: Int, user: User): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = user,
      options = new ReplaceOptions().upsert(false) // Set to false for strict updates
    ).toFuture().map { result =>
      if (result.getMatchedCount == 0)
        Left(APIError.BadAPIResponse(404, "User not found"))
      else
        Right(result)
    }.recover {
      case _ => Left(APIError.BadAPIResponse(500, "User cannot be updated"))
    }


  def delete(id: Int): Future[result.DeleteResult] =
    collection.deleteOne(
      filter = byID(id)
    ).toFuture()

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests

}
