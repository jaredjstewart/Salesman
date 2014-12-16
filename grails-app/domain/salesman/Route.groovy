package salesman

class Route {

    List<Integer> route
    List<Integer> breaks

    static constraints = {
       route blankable:true
        breaks blankable:true
    }
}
