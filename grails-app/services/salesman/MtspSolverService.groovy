package salesman

import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.simple.BetterSimpleMatrix


@GrailsCompileStatic
@Transactional
class MtspSolverService {
    static {
        println "yo"
        dmat = BetterSimpleMatrix.loadCSVfromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream('distances.csv'))
        xy = BetterSimpleMatrix.loadCSVfromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream('CITIES_AS_MATRIX.csv'))
    }

    static BetterSimpleMatrix dmat
    static BetterSimpleMatrix xy
    private Integer n = xy.numRows()
    private Integer nSalesman = 3
    private Integer minTour = 3
    private Integer popSize = 160
    Integer numIter = 100000
    private Integer nBreaks
    List<Double> cumProb
    BarebonesIntegerMatrix populationOfRoutes
    BarebonesIntegerMatrix populationOfBreaks
    private List<List<Integer>> popRoute = []
    private List<List<Integer>> popBreak = []
    private Random random = new Random()
    int[] optimalRoute
    int[] optimalBreaks
    public Double globalMin = 0
    public Integer currentIter = 0
    Boolean continueRunning = true

    void initializeParamsForBreakpointSelection() {
        nBreaks = nSalesman - 1
        Integer dof = n - minTour * nSalesman
        List<Integer> addTo = ones(dof + 1)
        for (k in 2..nBreaks) {
            addTo = cumsumList(addTo)
        }
        cumProb = div(cumsumList(addTo), addTo.sum())
    }

    void initializeThePopulations() {
        popRoute << (1..n).toList()
        popBreak << rand_breaks()

        for (i in (1..<popSize)) {
            popRoute << randperm(n)
            popBreak << rand_breaks()
        }

        populationOfRoutes = new BarebonesIntegerMatrix(popSize, n, popRoute)
        populationOfBreaks = new BarebonesIntegerMatrix(popSize, nBreaks, popBreak)
        println "done initializing population"
    }

    def solve() {
        Long start = System.currentTimeMillis()
        initializeParamsForBreakpointSelection()
        initializeThePopulations()

        globalMin = Double.MAX_VALUE
        double[] totalDists
        Double minDist
        Integer locationOfMinDist

        List<Integer> randomOrder = (List<Integer>) randperm(popSize).collect { Integer it -> it - 1 }
        int[] temporaryRoute = new int[n]
        int[] temporaryBreak = new int[nBreaks]

        BarebonesIntegerMatrix routesForThisGroup = new BarebonesIntegerMatrix(8, n)
        BarebonesIntegerMatrix breaksForThisGroup = new BarebonesIntegerMatrix(8, 2)
        double[] distances = new double[8]

        for (i in (1..numIter)) {
            //Evaluating members of the population
            currentIter = i

            totalDists = evaluateMembersOfPopulation()
            minDist = null;
            locationOfMinDist = null
            for (int j = 0; j < totalDists.size(); j++) {
                if (minDist == null || totalDists[j] < minDist) {
                    minDist = totalDists[j]
                    locationOfMinDist = j
                }
            }

            if (minDist < globalMin) {
                globalMin = minDist

                optimalRoute = populationOfRoutes.getRow(locationOfMinDist)
                optimalBreaks = populationOfBreaks.getRow(locationOfMinDist)
//                println "new best route iteration=$i d= ${minDist} }"
//                println "Route: $optimalRoute"
//                println "Breaks: $optimalBreaks"
            }


            //Genetic Algo Ops
            Collections.shuffle(randomOrder)
            for (int p = 0; p < popSize - 7; p += 8) {
                if (!continueRunning) {
                    return
                }
                List<Integer> orderForThisGroup = randomOrder[p..(p + 7)]

                int l = 0;
                for (int j : orderForThisGroup) {
                    routesForThisGroup.setRow(l, populationOfRoutes.getRow(j));
                    breaksForThisGroup.setRow(l, populationOfBreaks.getRow(j));
                    distances[l] = totalDists[j]
                    ++l
                }

                minDist = null;
                locationOfMinDist = null
                for (int j = 0; j < 8; j++) {
                    if (minDist == null || distances[j] < minDist) {
                        minDist = distances[j]
                        locationOfMinDist = j
                    }
                }
                int[] bestRouteOfThisGroup = routesForThisGroup.getRow(locationOfMinDist)
                int[] bestBreakOfThisGroup = breaksForThisGroup.getRow(locationOfMinDist)
                ///////////////////////////

                def routeInsertionPoints = (1..2).collect({ random.nextInt(n) }).sort()
                Integer I = routeInsertionPoints[0] as Integer
                Integer J = routeInsertionPoints[1] as Integer

                for (Integer k in 0..7) { // Generate new solutions
                    System.arraycopy(bestRouteOfThisGroup, 0, temporaryRoute, 0, bestRouteOfThisGroup.length);
                    System.arraycopy(bestBreakOfThisGroup, 0, temporaryBreak, 0, bestBreakOfThisGroup.length);

                    switch (k % 4) {
                    //case 0: //Keep route the same
                        case 1: //flip
                            temporaryRoute = RouteMutations.flipArray(temporaryRoute, I, J)
                            break
                        case 2: //swap endpoints
                            temporaryRoute = RouteMutations.swapArray(temporaryRoute, I, J)
                            break
                        case 3: //push 1 left
                            temporaryRoute = RouteMutations.pushArray(temporaryRoute, I, J)
                            break
                    }
                    if (k > 3) {
                        temporaryBreak = rand_breaks_array() //Half the population of mutants gets new breaks
                    }
                    populationOfRoutes.setRow(p + k, temporaryRoute)
                    populationOfBreaks.setRow(p + k, temporaryBreak)
                }
            }

        }

        Long end = System.currentTimeMillis()
        println "Algorithm complete."
        println "d= ${globalMin}"
        println "Time: ${(end - start) / 1000} seconds"
        println "Route: $optimalRoute"
        println "Breaks: $optimalBreaks"
    }

    double[] evaluateMembersOfPopulation() {
        double[] totalDists = new double[popSize]
        for (int p = 0; p < popSize; p++) {
            totalDists[p] = computeTotalDistanceOfRouteBreakArray(populationOfRoutes.getRow(p), populationOfBreaks.getRow(p))
        }
        return totalDists
    }

    static double computeDistanceOfSingleRouteArray(int[] route) {
        double d = 0
        d += dmat.get(route[route.length - 1] - 1, route[0] - 1)
        for (int i = 0; i < route.size() - 1; i++) {
            d += dmat.get(route[i] - 1, route[i + 1] - 1)
        }
        return d
    }

    static double computeTotalDistanceOfRouteBreakArray(int[] pRoute, int[] pBreak) {
        int start
        int end

        double d = 0
        int[] tmp= new int[pBreak[0]]
        System.arraycopy(pRoute, 0, tmp, 0, pBreak[0])
        d += computeDistanceOfSingleRouteArray(tmp)

        for (int p = 0; p < pBreak.length - 1; p++) {
            start = pBreak[p]
            end = pBreak[p + 1]
            tmp= new int[end - start]
            System.arraycopy(pRoute, start, tmp, 0, end - start )
            d+= computeDistanceOfSingleRouteArray(tmp)
        }

        start = pBreak[pBreak.length - 1]
        end = pRoute.length - 1
        tmp= new int[end - start]
        System.arraycopy(pRoute, start, tmp, 0, end - start)
        d+= computeDistanceOfSingleRouteArray(tmp)

        return d
    }



    static List<List<Integer>> splitIntoRoutes(List<Integer> pRoute, List<Integer> pBreak) {
        List<List<Integer>> rng = [] //Nested list, each member is a list representing a route
        rng.add((List<Integer>) pRoute.subList(0, pBreak.get(0)))
        for (int p = 0; p < pBreak.size() - 1; p++) {
            Integer start = pBreak[p]
            Integer end = pBreak[p + 1]
            rng.add((List<Integer>) pRoute.subList(start, end))// as List<Integer>)
        }
        rng.add((List<Integer>) pRoute.subList(pBreak.last(), pRoute.size()))
        return rng
    }


    List<Integer> rand_breaks(double rand = 0.0d) {
        return rand_breaks_array() as List<Integer>
    }

    int[] rand_breaks_array(double rand = 0.0d) {
        while (rand == 0.0d) {
            rand = random.nextDouble() //Matlab's rand excludes 0, so we should do so too
        }

        int nAdjust = -1
        for (int i = 0; i < cumProb.size(); i++) {
            if (rand < cumProb.get(i)) {
                nAdjust = i
                break
            }
        }
        int[] adjust = new int[nBreaks]
        for (int i = 0; i < nAdjust; i++) {
            adjust[random.nextInt(nBreaks)] += 1
        }

        int[] adjustArray = cumsumIntArray(adjust)

        int[] returnArray = new int[nBreaks]
        for (int i = 0; i < nBreaks; i++) {
            returnArray[i] = (i + 1) * minTour + adjustArray[i]
        }
        return returnArray
    }

    static BetterSimpleMatrix cumsum(BetterSimpleMatrix matrix) {
        if (matrix.numRows() != 1) {
            throw new RuntimeException("BetterSimpleMatrix.cumsum() is only implemented on row vectors")
        }
        BetterSimpleMatrix ret = new BetterSimpleMatrix(1, matrix.numCols())
        double total = 0;
        for (int i = 0; i < matrix.numCols(); i++) {
            total += matrix.get(0, i);
            ret.set(0, i, total);
        }
        return ret
    }

    static List<Integer> cumsumList(List<Integer> matrix) {
        int total = 0;
        def ret = []
        for (int i = 0; i < matrix.size(); i++) {
            total += (matrix.get(i));
            ret << total;
        }
        return ret
    }

    static int[] cumsumIntArray(int[] matrix) {
        int total = 0;
        int[] ret = new int[matrix.size()]

        for (int i = 0; i < matrix.size(); i++) {
            total += matrix[i];
            ret[i] = total;
        }
        return ret
    }

    static def sum(BetterSimpleMatrix matrix) {
        return matrix.elementSum()
    }

    static List<Integer> randperm(Integer n) {
        List<Integer> tmpBreaks = (1..n).toList()
        Collections.shuffle(tmpBreaks)
        return tmpBreaks
    }

    static List<Integer> ones(Integer n) {
        (1..n).toList()
    }

    static List<Double> div(List<Integer> list, Object x) {
        return list.collect { Integer it -> it / (Number) x }
    }

}
