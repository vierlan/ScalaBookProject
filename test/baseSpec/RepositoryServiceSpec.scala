package baseSpec

import models.{APIError, DataModel}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._
import repositories.{DataRepositoryTrait}
import services.RepositoryService

import scala.concurrent.{ExecutionContext, Future}

class RepositoryServiceSpec extends AnyWordSpec
  with MockFactory
  with ScalaFutures
  with GuiceOneAppPerSuite
  with Matchers {

  val mockDataRepo: DataRepositoryTrait = mock[DataRepositoryTrait]
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  val testRepoService = new RepositoryService(mockDataRepo)

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test subtitle",
    100
  )

  "RepositoryService.create" should {
    "return Right(DataModel) when the repository returns Right" in {
      (mockDataRepo.create _)
        .expects(dataModel)
        .returning(Future.successful(Right(dataModel)))

      whenReady(testRepoService.create(dataModel)) { result =>
        result shouldBe Right(dataModel)
      }
    }

    "return Left(APIError) when the repository returns Left" in {
      val apiError = APIError.BadAPIResponse(500, "Error when adding a book")

      (mockDataRepo.create _)
        .expects(dataModel)
        .returning(Future.successful(Left(apiError)))

      whenReady(testRepoService.create(dataModel)) { result =>
        result shouldBe Left(apiError)
      }
    }
  }
}
