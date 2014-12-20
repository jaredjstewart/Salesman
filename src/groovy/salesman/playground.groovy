package salesman

import org.codehaus.groovy.grails.io.support.ClassPathResource
import org.ejml.simple.SimpleMatrix

//println(new MtspSolverService().solve())

//def svc = new MtspSolverService()
//println Thread.currentThread().getContextClassLoader().getResource('distances.csv').path
//
SimpleMatrix dmat = SimpleMatrix.loadCSV(Thread.currentThread().getContextClassLoader().getResource('distances.csv').path);

//
// dmat.print()
println ((int) Double.MAX_VALUE)