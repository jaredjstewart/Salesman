package salesman

//println(new MultipleTSPSolver().solve())

def svc = new MtspSolverService()

def results = (1..100).collect{ svc.rand_breaks()}

println results*.first().min()
println results*.last().max()