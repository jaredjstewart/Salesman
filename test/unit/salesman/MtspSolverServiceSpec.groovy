package salesman

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MtspSolverService)
class MtspSolverServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {

        def pRoute= [5,6,9,1,4,2,8,10,3,7]
        def pBreak = [3,7]

        when:
        def result = service.splitIntoRoutes(pRoute,pBreak)
        then:
        result == [[5,6,9],[1,4,2,8],[10,3,7]]
    }
}
