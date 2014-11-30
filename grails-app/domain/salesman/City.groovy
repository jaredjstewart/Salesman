package salesman

import com.javadocmd.simplelatlng.LatLng

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
