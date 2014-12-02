package salesman

import com.javadocmd.simplelatlng.LatLng
import groovy.transform.Canonical
import groovy.transform.ToString

@ToString
class City {
    Integer id
    String name
    String state

    LatLng latLng

    public getLatLng() {
        return [latLng.latitude, latLng.longitude]
    }

    static constraints = {
    }


}
