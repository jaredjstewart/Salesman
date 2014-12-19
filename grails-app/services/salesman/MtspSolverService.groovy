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
    private List<List<Integer>> popRoute = []
    private List<List<Integer>> popBreak = []
    private Random random = new Random()
    List<Integer> optRoute
    List<Integer> optBreak
    public Double globalMin = 0
    public Integer currentIter = 0
    Boolean  continueRunning = true

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

        println "done initializing population"
    }

    def solve() {
        Long start = System.currentTimeMillis()
        initializeParamsForBreakpointSelection()
        initializeThePopulations()

        globalMin = Double.MAX_VALUE
        List<Double> totalDists
        Double minDist

        List<Integer> randomOrder = (List<Integer>) randperm(popSize).collect { Integer it -> it - 1}
        List<List<Integer>>  rtes, brks
        List<Double> dists
        List<Integer> tmpRoute = []
        List<Integer> tmpBreak = []

        for (i in (1..numIter)) {
            //Evaluating members of the population
            currentIter = i
            totalDists = evaluateMembersOfPopulation()
            minDist = totalDists.min()
            if (minDist < globalMin) {
                globalMin = minDist
                optRoute = popRoute.get(totalDists.indexOf(minDist))
                optBreak = popBreak.get(totalDists.indexOf(minDist))
            }


            //Genetic Algo Ops
            Collections.shuffle(randomOrder)
            for (int p = 0; p < popSize - 7; p += 8) {
                if (!continueRunning) {
                    return
                }
                List<Integer> orderForThisGroup =  (List<Integer>) randomOrder[p..(p+7)]
                rtes = (List<List<Integer>>) orderForThisGroup.collect {Integer it -> popRoute.get(it) }
                brks = (List<List<Integer>>) orderForThisGroup.collect {Integer it -> popBreak.get(it) }
                dists = orderForThisGroup.collect { Integer it -> totalDists.get(it) }
                Double distToIgnore = dists.min()
                int idxToIgnore = dists.indexOf(distToIgnore)
                List<Integer> bestOf8Route = rtes[idxToIgnore]
                List<Integer> bestOf8Break = brks[idxToIgnore]
                def routeInsertionPoints = (1..2).collect({ Math.floor(n * random.nextDouble()) }).sort()
                def I = routeInsertionPoints[0] as Integer
                def J = routeInsertionPoints[1] as Integer

                for (Integer k in 0..7) { // Generate new solutions
                    tmpRoute = new ArrayList<Integer>(bestOf8Route)
                    tmpBreak = new ArrayList<Integer>(bestOf8Break)
                    switch (k % 4) {
                    //case 0: //Keep route the same
                        case 1: //flip
                            tmpRoute = RouteMutations.flip(tmpRoute, I, J)
                            break
                        case 2: //swap endpoints
                            tmpRoute = RouteMutations.swap(tmpRoute, I, J)
                            break
                        case 3: //push 1 left
                            tmpRoute = RouteMutations.push(tmpRoute, I, J)
                            break
                    }
                    if (k > 3) {
                        tmpBreak = rand_breaks() //Half the population of mutants gets new breaks
                    }
                    popRoute.set(p+k,tmpRoute)
                    popBreak.set(p+k, tmpBreak)
                }
            }

        }

        //            tln "Breaks: $optBreak"
        Long  end = System.currentTimeMillis()
        println "Algorithm complete."
        println "d= ${globalMin}"
        println "Time: ${(end - start) / 1000} seconds"
        println "Route: $optRoute"
        splitIntoRoutes(optRoute, optBreak)
    }

    List<Double> evaluateMembersOfPopulation() {
        List<Double> totalDists = []
        for (Integer p = 0; p < popSize; p++) {
            List<Integer> pRoute = popRoute.get(p) //route to evaluate
            List<Integer> pBreak = popBreak.get(p) //break to evaluate

            totalDists[p]=(computeTotalDistanceOfRouteBreak(pRoute, pBreak))
        }
        return totalDists
    }


    static Double computeDistanceOfSingleRoute(List<Integer> route) {
        Double d = 0
        d += dmat.get(route.last() - 1, route.first() - 1)
        for (int i = 0; i < route.size() - 1; i++) {
            d += dmat.get(route.get(i) - 1, route.get(i + 1) - 1)
        }
        return d
    }


    static Double  computeTotalDistanceOfRouteBreak(List<Integer> pRoute, List<Integer> pBreak) {
        List<List<Integer>> routeOfEachSalesmen = splitIntoRoutes(pRoute, pBreak)
        double d = 0
        return (routeOfEachSalesmen.sum ({List<Integer> aRoute -> computeDistanceOfSingleRoute(aRoute) }) as Double)
    }

    static List<List<Integer>> splitIntoRoutes(List<Integer>  pRoute, List<Integer>  pBreak) {
        List<List<Integer>> rng = [] //Nested list, each member is a list representing a route
        rng.add((List<Integer>) pRoute.subList(0,pBreak.get(0)))
        for (int p = 0; p < pBreak.size() - 1; p++) {
            Integer start = pBreak[p]
            Integer end = pBreak[p + 1]
            rng.add((List<Integer>) pRoute.subList(start,end))// as List<Integer>)
        }
        rng.add( (List<Integer>) pRoute.subList(pBreak.last(),pRoute.size()))
        return rng
    }


    List<Integer> rand_breaks(double rand = 0.0d) {
        if (minTour == 1) {
            List<Integer> tmpBreaks = randperm(n)
            return  (List<Integer>) tmpBreaks.subList(0,nBreaks).sort()
        } else {
            while (rand == 0.0d) {
                rand = random.nextDouble() //Matlab's rand excludes 0, so we should do so too
            }
            int nAdjust = cumProb.findIndexOf { Double it -> it > rand }
            List<Integer> spaces = (1..nAdjust).collect {(Integer) Math.ceil(random.nextDouble() * nBreaks) }

            List<Integer> adjust2 = []
            for (i in 0..<nBreaks) {
                adjust2.add(spaces.findAll({ it == i + 1 }).size())
            }
            List<Integer> adjustList = cumsumList(adjust2)
            return   (List<Integer>) ((1..nBreaks).collect { Integer it -> it * minTour + adjustList.get(it -1)} )
        }
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
        return list.collect {Integer it -> it / (Number) x }
    }


}
