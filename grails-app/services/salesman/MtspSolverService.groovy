package salesman

import grails.transaction.Transactional
import org.ejml.simple.SimpleMatrix

@Transactional
class MtspSolverService {
    static {
        println "yo"
        SimpleMatrixMetaclassAdditions.apply()
    }

    static  SimpleMatrix dmat = SimpleMatrix.loadCSV(/C:\Projects\MultipleDepotVehicleRoutingProblem\ejmlData\distances.csv/);
    static SimpleMatrix xy = SimpleMatrix.loadCSV(/C:\Projects\MultipleDepotVehicleRoutingProblem\ejmlData\CITIES_AS_MATRIX.csv/);
    private def n = xy.numRows()
    private Integer nSalesman = 3
    private def minTour = 3
    private def popSize = 160
    private def numIter = 10000
    private def nBreaks
    private def cumProb
    private List<List<Integer>> popRoute = []
    private List<List<Integer>> popBreak = []
    private def random = new Random()
    def optRoute
    def optBreak

    void initializeParamsForBreakpointSelection() {
        nBreaks = nSalesman - 1
        def dof = n - minTour * nSalesman
        def addTo = ones(dof + 1)
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

        println "done"
    }

    def solve() {

        initializeParamsForBreakpointSelection()
        initializeThePopulations()

        def globalMin = Double.MAX_VALUE
        List<Double> totalDists
        double minDist

        def randomOrder = randperm(popSize)*.minus(1)
        def rtes, brks, dists
        List<Integer> tmpRoute = []
        List<Integer> tmpBreak = []

        for (i in (1..numIter)) {
            //Evaluating members of the population
            totalDists = evaluateMembersOfPopulation()
            minDist = totalDists.min()
            if (minDist < globalMin) {
                globalMin = minDist
                optRoute = popRoute.get(totalDists.indexOf(minDist))
                optBreak = popBreak.get(totalDists.indexOf(minDist))
                println "new best route iteration=$i d= ${minDist} ${splitIntoRoutes(optRoute, optBreak)}"
            }


            //Genetic Algo Ops
            Collections.shuffle(randomOrder)
            for (int p = 0; p < popSize - 7; p += 8) {
                def orderForThisGroup = randomOrder[p..(p+7)]
                rtes = orderForThisGroup.collect { popRoute.get(it) }
                brks = orderForThisGroup.collect { popBreak.get(it) }
                dists = orderForThisGroup.collect { totalDists[it] }
                def distToIgnore = dists.min()
                int idxToIgnore = dists.indexOf(distToIgnore)
                List<Integer> bestOf8Route = rtes[idxToIgnore]
                List<Integer> bestOf8Break = brks[idxToIgnore]
                def routeInsertionPoints = (1..2).collect({ Math.floor(n * random.nextDouble()) }).sort()
                def I = routeInsertionPoints[0] as Integer
                def J = routeInsertionPoints[1] as Integer

                for (k in 0..7) { // Generate new solutions
                    tmpRoute = new ArrayList(bestOf8Route)
                    tmpBreak = new ArrayList(bestOf8Break)
                    switch (k % 4) {
                    //case 0: //Keep route the same
                        case 1: //flip
//                            println "flip"
                            tmpRoute = RouteMutations.flip(tmpRoute, I, J)
                            break
                        case 2: //swap endpoints
//                            println "swap"
                            tmpRoute = RouteMutations.swap(tmpRoute, I, J)
                            break
                        case 3: //push 1 left
//                            println "push"
                            tmpRoute = RouteMutations.push(tmpRoute, I, J)
                            break
                    }
                    if (k > 3) {
                        tmpBreak = rand_breaks() //Half the population of mutants gets new breaks
                    }
                    popRoute[p+k] = tmpRoute
                    popBreak[p+k] = tmpBreak
                }
                1+1
            }

        }

        println "Algorithm complete."
        splitIntoRoutes(optRoute, optBreak)
    }

    List<Double> evaluateMembersOfPopulation() {
        def totalDists = []
        for (p in (0..<popSize)) {
            List pRoute = popRoute.get(p) //route to evaluate
            List pBreak = popBreak.get(p) //break to evaluate
            totalDists << computeTotalDistanceOfRouteBreak(pRoute, pBreak)
        }
        return totalDists
    }


    static double computeDistanceOfSingleRoute(List<Integer> route) {
        double d = 0
        d += dmat[route.last() - 1, route.first() - 1]
        for (int i = 0; i < route.size() - 1; i++) {
            d += dmat[route.get(i) - 1, route.get(i + 1) - 1]
        }
        return d
    }


    static double  computeTotalDistanceOfRouteBreak(List pRoute, List pBreak) {
        ArrayList<ArrayList<Integer>> routeOfEachSalesmen = splitIntoRoutes(pRoute, pBreak)
        double d = 0
        return routeOfEachSalesmen.sum { aRoute -> computeDistanceOfSingleRoute(aRoute) }
    }

    static ArrayList<ArrayList<Integer>> splitIntoRoutes(List pRoute, List pBreak) {
        ArrayList<ArrayList<Integer>> rng = [] //Nested list, each member is a list representing a route
        rng << pRoute[0..<pBreak[0]]
        for (int p = 0; p < pBreak.size() - 1; p++) {
            def start = pBreak[p]
            def end = pBreak[p + 1]
            rng << pRoute[start..<end] as List<Integer>
        }
        rng << pRoute[pBreak.last()..<pRoute.size()]
        return rng
    }


    ArrayList<Integer> rand_breaks(double rand = 0.0d) {
        if (minTour == 1) {
            def tmpBreaks = randperm(n)
            return tmpBreaks[0..<nBreaks].sort()
        } else {
            while (rand == 0.0d) {
                rand = random.nextDouble() //Matlab's rand excludes 0, so we should do so too
            }
            def nAdjust = cumProb.findIndexOf { it > rand }
            def spaces = (1..nAdjust).collect { Math.ceil(random.nextDouble() * nBreaks) }
            def adjust = SimpleMatrix.zeros(1, nBreaks)
            for (i in 0..<nBreaks) {
                adjust[0, i] = (spaces.findAll({ it == i + 1 }).size())
            }
            return (cumsum(SimpleMatrix.ones(1, nBreaks)) * minTour + cumsum(adjust)).getRowAsList(0)
        }
    }


    static SimpleMatrix cumsum(SimpleMatrix matrix) {
        if (matrix.numRows() != 1) {
            throw new RuntimeException("SimpleMatrix.cumsum() is only implemented on row vectors")
        }
        SimpleMatrix ret = new SimpleMatrix(1, matrix.numCols())
        int total = 0;
        for (int i = 0; i < matrix.numCols(); i++) {
            total += matrix[0, i];
            ret[0, i] = total;
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

    static def sum(SimpleMatrix matrix) {
        return matrix.elementSum()
    }

    static List<Integer> randperm(Integer n) {
        def tmpBreaks = (1..n).toList()
        Collections.shuffle(tmpBreaks)
        return tmpBreaks
    }

    static List<Integer> ones(Integer n) {
        (1..n).collect {
            1
        }
    }

    static List<Double> div(def list, def x) {
        return list.collect { it / x }
    }


}
