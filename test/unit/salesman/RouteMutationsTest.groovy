package salesman

import spock.lang.Specification


class RouteMutationsTest extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    def "test flip"() {
        given:
        def route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.flip(route, 2, 5)
        then:
        result == [1, 2, 6, 5, 4, 3, 7]
    }

    def "test flip array"() {
        given:
        int[] route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.flipArray(route, 2, 5)
        then:
        result == [1, 2, 6, 5, 4, 3, 7] as int[]
    }

    def "test swap"() {
        given:
        def route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.swap(route, 2, 5)
        then:
        result == [1, 2, 6, 4, 5, 3, 7]
    }
    def "test swap array"() {
        given:
        int[] route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.swapArray(route, 2, 5)
        then:
        result == [1, 2, 6, 4, 5, 3, 7] as int[]
    }

    def "test push"() {
        given:
        def route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.push(route, 2, 5)
        then:
        result == [1, 2, 6, 3, 4, 5, 7]
    }
    def "test push array"() {
        given:
        int[] route = [1,2,3,4,5,6,7]
        when:
        def result =  RouteMutations.pushArray(route, 2, 5)
        then:
        result == [1, 2, 6, 3, 4, 5, 7] as int[]
    }
}
