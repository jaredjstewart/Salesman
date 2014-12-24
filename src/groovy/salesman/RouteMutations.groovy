package salesman

import groovy.transform.CompileStatic

@CompileStatic
class RouteMutations {
    static int[] flipArray(int[] route, int I, int J) {
        int[] ret = new int[route.length]

        System.arraycopy(route, 0, ret, 0, route.length)

        for (int i = I; i < J + 1; i++){
                ret[i] = route[J - (i - I)]
            }

        return ret
    }

    static int[] swapArray(int[] route, int I, int J) {
        int[] ret = new int[route.length]

        System.arraycopy(route, 0, ret, 0, route.length)
        ret[I] = route[J]
        ret[J] = route[I]

        return ret
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
