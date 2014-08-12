package hareball

import com.ning.http.client.{ AsyncHandler, Response }
import dispatch.{ FunctionHandler, Http, Req }
import scala.concurrent.{ Future, ExecutionContext }

object Client {
  type Handler[T] = AsyncHandler[T]
}

case class Credentials(user: String, password: String) {
  def sign(req: Req) = req.as_!(user, password)
}

case class Client(
  host: String, port: Int,
  credentials: Credentials,
  http: Http = new Http)
 (implicit ec: ExecutionContext) {
  def request[T]
    (req: Req)
    (handler: Client.Handler[T]): Future[T] =
     http(credentials.sign(req) > handler)
}
