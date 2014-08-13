package hareball

import com.ning.http.client.{ AsyncHandler, Response }
import dispatch.{ OkFunctionHandler, Http, Req, :/ }
import scala.concurrent.{ Future, ExecutionContext }

object Client {
  type Handler[T] = AsyncHandler[T]
  abstract class Completion[T: Rep] {
    def apply(): Future[T] =
      apply(implicitly[Rep[T]].map)
    def apply[T]
      (f: Response => T): Future[T] =
       apply(new OkFunctionHandler(f))
    def apply[T]
      (handler: Client.Handler[T]): Future[T]
  }
}

abstract class Requests(
  val api: Req, http: Http)
 (implicit ec: ExecutionContext)
  extends Methods {
  def request[T]
    (req: Req)
    (handler: Client.Handler[T]): Future[T] =
     http(req > handler)
  def complete[T: Rep]
   (req: Req): Client.Completion[T] =
    new Client.Completion[T] {
      def apply[T](handler: Client.Handler[T]) =
        request(req)(handler)
    }
}

case class Client(
  host: String = "localhost", port: Int = 55672,
  private val credentials: Credentials = Credentials.None,
  private val http: Http = new Http)
 (implicit ec: ExecutionContext)
  extends Requests(
    credentials.sign(:/(host, port) / "api")
    <:< Map("Content-Type" -> "application/json"), http
  )
