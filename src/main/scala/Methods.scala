package hareball

trait Methods { self: Requests =>

  object vhosts {
    def base = api / "vhosts"
    def list = request(base)_
    def get(name: String) = request(base / name)_
    def delete(name: String) = request(base.DELETE / name)_
    def put(name: String) = request(base.PUT / name)_    
  }

  object exchanges {
    def base = api / "exchanges"
    def list = request(base)_
    def get(name: String) = request(base / name)_
    def delete(name: String) = request(base.DELETE / name)_
    def put(name: String) = request(base.PUT / name)_    
  }

}
