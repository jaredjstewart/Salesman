package salesman

class RouteMutations {
    static List<Integer> flip(List<Integer> route, int I, int J) {
        List<Integer> front = (I > 0) ? route[0..<I] : []
        List<Integer> middle = route[I..J]
        List<Integer> end = (J + 1 < route.size()) ? route[(J + 1)..-1] : []

        return front + middle.reverse() + end
    }

    static List<Integer> swap(List<Integer> route, int I, int J) {
        Collections.swap(route, I, J)

        return route
    }

    static List<Integer> push(List<Integer> route, int I, int J) {
        List<Integer> front = (I > 0) ? route[0..<I] : []
        List<Integer> middle = route[I..J]
        List<Integer> end = (J + 1 < route.size()) ? route[(J + 1)..-1] : []
        int x = middle.pop()

        return front + x + middle + end
    }

}
