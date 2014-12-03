package salesman

import groovy.transform.ToString

@ToString
class City {
    Integer id
    String name
    String state

    double latitude
    double longitude

    static constraints = {
    }

}
