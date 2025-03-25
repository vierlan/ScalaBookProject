//package services
//
//import com.google.inject.ImplementedBy
//import models.{APIError, DataModel}
//import org.mongodb.scala.result
//import play.shaded.ahc.io.netty.util.DefaultAttributeMap
//import repositories.DataRepository
//
//import javax.inject.Inject
//import scala.concurrent.Future
//
//
//class RepositoryService @Inject()(dataRepository: DataRepository) {
//
//  def index(): Future[Either[APIError, Seq[DataModel]]] = dataRepository.index()
//  def read(id: String): Future[DataModel] = dataRepository.read(id)
//  def create(book:DataModel): Future[DataModel] = dataRepository.create(book)
//  def update(id: String, book: DataModel): Future[result.UpdateResult] = dataRepository.update(id, book)
//  def delete(id: String): Future[result.DeleteResult] = dataRepository.delete(id)
//  def byID(id: String): Future[DataModel] = dataRepository.byID(id)
//
//}

package services

import models.{APIError, DataModel}
import org.mongodb.scala.result
import repositories.DataRepositoryTrait

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositoryService @Inject()(val dataRepository: DataRepositoryTrait)(implicit ec: ExecutionContext) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
    dataRepository.index()

  def read(id: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] =
    dataRepository.read(id)

  def create(book: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.create(book)

  def update(id: String, book: DataModel): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    dataRepository.update(id, book)

  def delete(id: String): Future[Either[APIError.BadAPIResponse, result.DeleteResult]] =
    dataRepository.delete(id)

 // def readByName(name: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] =
   // dataRepository.readByName(name)

  //def updateField(id: String, field: String, value: String): Future[Either[APIError.BadAPIResponse, result.UpdateResult]] =
    //dataRepository.updateField(id, field, value)

  //def readByISBN(isbn: String): Future[Either[APIError.BadAPIResponse, Option[DataModel]]] =
   // dataRepository.readByISBN(isbn)
}

