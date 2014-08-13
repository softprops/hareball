package hareball

import com.ning.http.client.Response
import dispatch.as
import org.json4s._

case class VirtualHost(name: String, tracing: Boolean)

case class Exchange(
  name: String,
  vhost: String,
  typ: String,
  durable: Boolean = true,
  autoDelete: Boolean = false,
  internal: Boolean = false,
  arguments: Map[String, String] = Map.empty[String, String]
)

case class ExchangeDetails(
  name: String,
  vhost: String,
  typ: String,
  durable: Boolean = true,
  autoDelete: Boolean = false,
  internal: Boolean = false,
  arguments: Map[String, String] = Map.empty[String, String],
  incoming: List[String] = Nil,
  outgoing: List[String] = Nil
)

/** type class for default representations */
sealed trait Rep[T] {
  def map: Response => T
}

object Rep {
  implicit val Identity: Rep[Response] = new Rep[Response] {
    def map = identity(_)
  }

  implicit val Nada: Rep[Unit] = new Rep[Unit] {
    def map = _ => ()
  }

  implicit val ExchangeDefaults: Rep[ExchangeDetails] =
    new Rep[ExchangeDetails] {
      def map = as.json4s.Json andThen { js =>
        (for {
          JObject(exchange)                  <- js
          ("name", JString(name))            <- exchange
          ("vhost", JString(vhost))          <- exchange
          ("type", JString(typ))             <- exchange
          ("durable", JBool(durable))        <- exchange
          ("auto_delete", JBool(autoDelete)) <- exchange
          ("internal", JBool(internal))      <- exchange
        } yield ExchangeDetails(
          name, vhost, typ, durable, autoDelete, internal)).head
      }
    }

  implicit object Exchanges extends Rep[List[Exchange]] {
    def map = as.json4s.Json andThen(for {
      JArray(exchanges)                  <- _
      JObject(exchange)                  <- exchanges
      ("name", JString(name))            <- exchange
      ("vhost", JString(vhost))          <- exchange
      ("type", JString(typ))             <- exchange
      ("durable", JBool(durable))        <- exchange
      ("auto_delete", JBool(autoDelete)) <- exchange
      ("internal", JBool(internal))      <- exchange
    } yield Exchange(name, vhost, typ, durable, autoDelete, internal))
  }

  implicit val vhost: Rep[VirtualHost] =
    new Rep[VirtualHost] {
      def map = as.json4s.Json andThen { js =>
        (for {
          JObject(host)               <- js
          ("name", JString(name))     <- host
          ("tracing", JBool(tracing)) <- host
        } yield VirtualHost(name, tracing)).head
      }
    }

  implicit val listOfVHosts: Rep[List[VirtualHost]] =
    new Rep[List[VirtualHost]] {
      def map = as.json4s.Json andThen(for {
        JArray(hosts)               <- _
        JObject(host)               <- hosts
        ("name", JString(name))     <- host
        ("tracing", JBool(tracing)) <- host
      } yield VirtualHost(name, tracing))
    }
}
