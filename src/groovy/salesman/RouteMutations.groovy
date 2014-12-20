package salesman

import groovy.transform.CompileStatic

@CompileStatic
class RouteMutations {
    static int[] flipArray(int[] route, int I, int J) {

        int[] ret = new int[route.length]

        for (int i = 0; i < route.length; i++){
            if (i < I) {
                ret[i] = route[i]
            } else if (i > J) {
                ret [i] = route[i]
            } else {
                ret[i] = route[J - (i - I)]
            }

        }
        return ret
    }

    static int[] swapArray(int[] route, int I, int J) {
        int tmp = route[I]
        route[I] = route[J]
        route[J] = tmp

        return route
    }

    static int[] pushArray(int[] route, int I, int J) {
        int[] ret = new int[route.length]

        for (int i = 0; i < route.length; i++) {
            if (i < I) {
                ret[i] = route[i]
            } else if (i > J) {
                ret[i] = route[i]
            } else if (i == I) {
                ret[i] = route[J]
            }else {
                ret[i] = route[i - 1]
            }
        }
        return ret
    }

}
