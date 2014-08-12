package hareball

import com.ning.http.client.{ AsyncHandler, Response }
import dispatch.{ FunctionHandler, Http, Req, :/ }
import scala.concurrent.{ Future, ExecutionContext }

object Client {
  type Handler[T] = AsyncHandler[T]
}

case class Credentials(user: String, password: String) {
  def sign(req: Req) = req.as_!(user, password)
}

abstract class Requests(
  val api: Req, http: Http)
 (implicit ec: ExecutionContext)
  extends Methods {
  def request[T]
    (req: Req)
    (handler: Client.Handler[T]): Future[T] =
     http(req > handler)
}

case class Client(
  host: String, port: Int,
  private val credentials: Credentials,
  private val http: Http = new Http)
 (implicit ec: ExecutionContext)
  extends Requests(credentials.sign(:/(host, port) / "api"), http)
