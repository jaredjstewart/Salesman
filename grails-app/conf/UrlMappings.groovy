import salesman.MapController

class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'map', action:"index")
        "500"(view:'/error')
	}
}
