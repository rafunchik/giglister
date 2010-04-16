class UrlMappings {
  static mappings = {
    "/"
    {
      controller = "gigGoer"
      action = "login"
    }
    "/$controller/$action?/$id?"{
        constraints {
          // apply constraints here
          }
      }
      "500"(view:'/error')
    }


}
