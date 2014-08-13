package hareball

import com.ning.http.client.Response

// http://hg.rabbitmq.com/rabbitmq-management/raw-file/3646dee55e02/priv/www-api/help.html

trait Methods { self: Requests =>

  def overview =
    complete[Response](api / "overview")

  object connections {
    def base = api / "connections"
    def list =
      complete[Response](base)
    def delete(name: String) =
      complete[Response](base.DELETE / name)
  }

  object channels {
    def base = api / "channels"
    def list =
      complete[Response](base)
    def get(name: String) =
      complete[Response](base / name)
  }

  object exchanges {
    def base = api / "exchanges"
    case class VHost(vhost: String) {
      def list =
        complete[List[Exchange]](base / vhost)
      def get(name: String) =
        complete[ExchangeDetails](base / vhost / name)
      def delete(name: String) =
        complete[Unit](base.DELETE / vhost / name)
      def put(name: String) =
        complete[Unit](base.PUT / vhost / name)
    }
    def list = complete[List[Exchange]](base)
    def vhost(name: String) = VHost(name)
  }

  object queues {
    def base = api / "queues"
    case class VHost(vhost: String) {
      def list =
        complete[Response](base / vhost)
      def get(name: String) =
        complete[Response](base / vhost / name)
      def delete(name: String) =
        complete[Unit](base.DELETE / vhost / name)
      def put(name: String) =
        complete[Unit](base.PUT / vhost / name)
    }
    def list = complete[Response](base)
    def vhost(name: String) = VHost(name)
  }

  object binding {
    def base = api / "bindings"
    case class VHost(vhost: String) {
      case class Between(queue: String, exchange: String) {
        def list = complete[Response](base / vhost / queue / exchange)
        //def post()
      }
      def list =
        complete[Response](base / vhost)
      def between(queue: String, exchange: String) =
        Between(queue, exchange)
    }
    def list = complete[Response](base)
    def vhost(name: String) = VHost(name)
  }

  object vhosts {
    def base = api / "vhosts"
    def list =
      complete[List[VirtualHost]](base)
    def get(name: String) =
      complete[VirtualHost](base / name)
    def delete(name: String) =
      complete[Unit](base.DELETE / name)
    def put(name: String) =
      complete[Unit](base.PUT / name)
  }

  object users {
    def base = api / "users"
    def list =
      complete[Response](base)
    def get(name: String) =
      complete[Response](base / name)
    def delete(name: String) =
      complete[Unit](base.DELETE / name)
    def put(name: String) =
      complete[Unit](base.PUT / name)
  }
}
