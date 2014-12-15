package salesman

import com.javadocmd.simplelatlng.LatLng
import grails.transaction.Transactional

import java.util.concurrent.Callable

@Transactional
class AnalysisJobService {
    def executorService

    def create() {

        new City(name:"abc", state:"tx", latitude: 1.0, longitude: 1.0).save()

        executorService.submit(
                {
                    println "yo1"
                    new City(name:"def", state:"tx", latitude: 1.0, longitude: 1.0).save()
                    sleep(5000)
                    println "yo2"
                } as Callable)
        sleep(1000)
        println City.list()
    }

}
