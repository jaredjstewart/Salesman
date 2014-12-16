package salesman

import grails.transaction.Transactional
import org.ejml.simple.SimpleMatrix


class SolveMTSPJob {

    def mtspSolverService

    def execute(context) {
//        def id = context.mergedJobDataMap.get('id')
//        println "Running with id $id"
//        def routeId = Route.get(id).id
//       mtspSolverService.solve(routeId)
        try {
            mtspSolverService.solve()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    static triggers = {
//      simple repeatInterval: 5000l // execute job once in 5 seconds
    }




}
