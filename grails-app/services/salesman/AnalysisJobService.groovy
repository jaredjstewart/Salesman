package salesman

import com.javadocmd.simplelatlng.LatLng
import grails.transaction.Transactional

import java.util.concurrent.Callable

@Transactional
class AnalysisJobService {
    def executorService

    def create() {

        new City(name:"abc", state:"tx", latitude: 1.0, longitude: 1.0).save()

        println City.list()
        executorService.submit(
                {
                    println "yo1"
                    sleep(10000)
                    println "yo2"
                } as Callable)
    }

}
