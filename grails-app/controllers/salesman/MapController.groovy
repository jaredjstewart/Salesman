package salesman

import groovy.json.JsonBuilder
import com.javadocmd.simplelatlng.LatLng
import com.xlson.groovycsv.CsvParser
import groovy.json.JsonBuilder

class MapController {

    def index() {

        def csv = new File(/C:\Projects\MutipleDepotVehicleRoutingProblem\data\cities.csv/).text

        def cities= new CsvParser().parse(csv, separator: ',', quoteChar: /"/).collect { city->
            new City(name: city.City, state:city.State,latLng: new LatLng(city.Latitude as double, -(city.Longitude as double)))
        }

        cities.eachWithIndex { City city, int i ->
            city.id = i
        }

        def json = new JsonBuilder(cities)


    }
}
