package salesman

import grails.transaction.Transactional

import java.util.concurrent.Callable

@Transactional
class AnalysisJobService {
    def executorService

    def create() {

        executorService.submit(
                {
                    println "yo1"
                    println executorService.dump()
                    sleep(10000)
                    println "yo2"
                } as Callable)

    }
}
