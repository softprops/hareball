package hareball

import dispatch.Req

trait Credentials {
  def sign(req: Req): Req
}

object Credentials {
  object None extends Credentials {
    def sign(req: Req) = req
  }
  case class Provided(user: String, password: String)
    extends Credentials {
    def sign(req: Req) = req.as_!(user, password)
  }  

  val guest = apply("guest", "guest")

  def apply(user: String, password: String) =
    Provided(user, password)
}
