package salesman

import grails.transaction.Transactional
import org.ejml.simple.SimpleMatrix

//@Transactional
class MtspSolverService {
    static scope = "singleton"
    static transactional = false
    static {
        println "yo"
        SimpleMatrixMetaclassAdditions.apply()
    }



    SimpleMatrix dmat = SimpleMatrix.loadCSV(/C:\Projects\MultipleDepotVehicleRoutingProblem\ejmlData\distances.csv/);
    SimpleMatrix xy = SimpleMatrix.loadCSV(/C:\Projects\MultipleDepotVehicleRoutingProblem\ejmlData\CITIES_AS_MATRIX.csv/);
    private def n = xy.numRows()
    private Integer nSalesman = 3
    private def minTour = 3
    private def popSize = 160
    private def numIter = 10
    private def nBreaks
    private def cumProb
    private List<List<Integer>> popRoute = []
    private List<List<Integer>> popBreak = []
    private def random = new Random()

    void initializeParamsForBreakpointSelection() {
        nBreaks = nSalesman - 1
        def dof = n - minTour * nSalesman
        def addTo = ones( dof + 1)
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
    }

    def solve( Long routeId) {

        initializeParamsForBreakpointSelection()
        initializeThePopulations()

        def globalMin = Double.MAX_VALUE
        List<Double> totalDists
        double minDist
        def optRoute
        def optBreak
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
                Route route = Route.get(routeId)
                route.route = optRoute
                route.breaks = optBreak
                println "About to save route: ${route.dump()}"
                if (!route.save(flush: true)){
                    println "Save failed due to errors: $route.errors"
                }

                println "new best route iteration=$i d= ${minDist} ${splitIntoRoutes(optRoute, optBreak)}"

            }

            //Genetic Algo Ops
            Collections.shuffle(randomOrder)
            for (int p = 7; p < popSize; p += 8) {
                def orderForThisGroup = randomOrder[(p - 7)..p]
                rtes = orderForThisGroup.collect { popRoute.get(it) }
                brks = orderForThisGroup.collect { popBreak.get(it) }
                dists = orderForThisGroup.collect { totalDists[p] }
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
                    switch (k %4) {
                    //case 0: //Keep route the same
                        case 1: //flip
                            tmpRoute = RouteMutations.flip(bestOf8Route, I, J)
                            break
                        case 2: //swap endpoints
                            tmpRoute = RouteMutations.swap(bestOf8Route, I, J)
                            break
                        case 3: //push 1 left
                            tmpRoute = RouteMutations.push(bestOf8Route, I, J)
                            break
                    }
                    if (k > 3) {
                        tmpBreak = rand_breaks() //Half the population of mutants gets new breaks
                    }
                    popRoute[p] =  tmpRoute
                    popBreak[p] = tmpBreak
                }
            }

        }

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


    double computeDistanceOfSingleRoute(List<Integer> route) {
        double d = 0
        d += dmat[route.last() - 1, route.first() - 1]
        for (int i = 0; i < route.size() - 1; i++) {
            d += dmat[route.get(i) - 1, route.get(i + 1) - 1]
        }
        return d
    }


    double computeTotalDistanceOfRouteBreak(List pRoute, List pBreak) {
        ArrayList<ArrayList<Integer>> routeOfEachSalesmen = splitIntoRoutes(pRoute, pBreak)
        double d = 0
        return routeOfEachSalesmen.sum { aRoute -> computeDistanceOfSingleRoute(aRoute) }
    }

    ArrayList<ArrayList<Integer>> splitIntoRoutes(List pRoute, List pBreak) {
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
        (1..n).collect{
            1
        }
    }

    static List<Double> div (def list, def x) {
        return list.collect {it / x}
    }

}
