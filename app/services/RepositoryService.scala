package services

import com.google.inject.ImplementedBy
import models.{APIError, DataModel}
import org.mongodb.scala.result
import play.shaded.ahc.io.netty.util.DefaultAttributeMap
import repositories.DataRepository

import javax.inject.Inject
import scala.concurrent.Future


class RepositoryService @Inject()(dataRepository: DataRepository) {

  def index(): Future[Either[APIError, Seq[DataModel]]] = dataRepository.index()
  def read(id: String): Future[DataModel] = dataRepository.read(id)
  def create(book:DataModel): Future[DataModel] = dataRepository.create(book)
  def update(id: String, book: DataModel): Future[result.UpdateResult] = dataRepository.update(id, book)
  def delete(id: String): Future[result.DeleteResult] = dataRepository.delete(id)
  def byID(id: String): Future[DataModel] = dataRepository.byID(id)

}
