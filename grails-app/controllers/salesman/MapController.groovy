package salesman

class MapController {

    def analysisJobService

    def updatePoints() {
        def route = (0..50).toList()
        def breaks = [5, 10]

        render(contentType: "application/json") {
            [route:route, breaks:breaks]
        }
    }

    def index() {
        def points = [49, 5, 31, 3, 43, 32, 4, 36, 46, 12, 25, 27, 40, 6, 38, 18, 29, 20, 45, 33, 37, 30, 8, 19, 7, 44, 26, 39, 10, 9, 1, 24, 17, 42, 35, 2, 41, 16, 14, 13, 23, 15, 28, 11, 22, 47, 21, 34, 48].collect {
            it - 1
        }
        def breaks = [13, 20]
        def depot1 = (points.subList(0, breaks[0]))

        println depot1
        def depot2 = (points.subList(breaks[0], breaks[1]))
        def depot3 = (points.subList(breaks[1], points.size()))

//        analysisJobService.create()

//    def points = [1,2,3,4]
//        def csv = new File(/C:\Projects\MutipleDepotVehicleRoutingProblem\data\cities.csv/).text
//
//        def cities= new CsvParser().parse(csv, separator: ',', quoteChar: /"/).collect { city->
//            new City(name: city.City, state:city.State,latLng: new LatLng(city.Latitude as double, -(city.Longitude as double)))
//        }
////
//        cities.eachWithIndex { City city, int i ->
//            city.id = i
//        }

//        cities*.save()
//        def json = new JsonBuilder(cities)
        [model: [points: points, breaks: breaks]]
    }

}
