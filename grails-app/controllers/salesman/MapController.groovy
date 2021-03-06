package salesman


class MapController {

    def mtspSolverService

    def updatePoints(Integer routeId) {

//        println "******$routeId"
        List<Integer> route = mtspSolverService.optimalRoute
        List<Integer> breaks = mtspSolverService.optimalBreaks

//        def route = [49,5,31,3,43,32,4,36,46,12,25,27,40,6,38,18,29,20,45,33,37,30,8,19,7,44,26,39,10,9,1,24,17,42,35,2,41,16,14,13,23,15,28,11,22,47,21,34,48]
//        def breaks = [13,20]
        render(contentType: "application/json") {
            [route:route.collect({it-1}), breaks:breaks, distance: mtspSolverService.globalMin, iteration: mtspSolverService.currentIter]
        }
    }


    def start() {
        mtspSolverService.continueRunning = true
        SolveMTSPJob.triggerNow()
    }

    def stop() {
        mtspSolverService.continueRunning = false
    }
    def index() {
        def points = [49, 5, 31, 3, 43, 32, 4, 36, 46, 12, 25, 27, 40, 6, 38, 18, 29, 20, 45, 33, 37, 30, 8, 19, 7, 44, 26, 39, 10, 9, 1, 24, 17, 42, 35, 2, 41, 16, 14, 13, 23, 15, 28, 11, 22, 47, 21, 34, 48].collect {
            it - 1
        }
        def breaks = [13, 20]
        def depot1 = (points.subList(0, breaks[0]))
        def depot2 = (points.subList(breaks[0], breaks[1]))
        def depot3 = (points.subList(breaks[1], points.size()))

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
        [model: [points: points, breaks: breaks, iteration: 0, distance: 0]]
    }

}
