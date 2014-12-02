//= require jquery
//= require jquery-jvectormap-1.2.2.min.js
//= require jvectormap-us-aea-en.js
//= require svg.js
//= require svg.path.js


var markerArray = [
    {
        "latLng": [
            32.354,
            -86.28399999999999
        ],
        "state": "AL",
        "name": "Montgomery "
    },
    {
        "latLng": [
            34.722,
            -92.354
        ],
        "state": "AR",
        "name": "Little Rock "
    },
    {
        "latLng": [
            33.543,
            -112.071
        ],
        "state": "AZ",
        "name": "Phoenix "
    },
    {
        "latLng": [
            38.567,
            -121.467
        ],
        "state": "CA",
        "name": "Sacramento "
    },
    {
        "latLng": [
            39.768,
            -104.87299999999999
        ],
        "state": "CO",
        "name": "Denver "
    },
    {
        "latLng": [
            41.766,
            -72.684
        ],
        "state": "CT",
        "name": "Hartford "
    },
    {
        "latLng": [
            38.905,
            -77.01599999999999
        ],
        "state": "DC",
        "name": "Washington "
    },
    {
        "latLng": [
            39.159,
            -75.517
        ],
        "state": "DE",
        "name": "Dover "
    },
    {
        "latLng": [
            30.456999999999997,
            -84.28099999999999
        ],
        "state": "FL",
        "name": "Tallahassee "
    },
    {
        "latLng": [
            33.763,
            -84.423
        ],
        "state": "GA",
        "name": "Atlanta "
    },
    {
        "latLng": [
            41.577,
            -93.61699999999999
        ],
        "state": "IA",
        "name": "Des Moines "
    },
    {
        "latLng": [
            43.607,
            -116.226
        ],
        "state": "ID",
        "name": "Boise City "
    },
    {
        "latLng": [
            39.781,
            -89.645
        ],
        "state": "IL",
        "name": "Springfield "
    },
    {
        "latLng": [
            39.775999999999996,
            -86.146
        ],
        "state": "IN",
        "name": "Indianapolis"
    },
    {
        "latLng": [
            39.038,
            -95.692
        ],
        "state": "KS",
        "name": "Topeka "
    },
    {
        "latLng": [
            38.190999999999995,
            -84.865
        ],
        "state": "KY",
        "name": "Frankfort "
    },
    {
        "latLng": [
            30.448999999999998,
            -91.12599999999999
        ],
        "state": "LA",
        "name": "Baton Rouge "
    },
    {
        "latLng": [
            42.336,
            -71.018
        ],
        "state": "MA",
        "name": "Boston "
    },
    {
        "latLng": [
            38.972,
            -76.503
        ],
        "state": "MD",
        "name": "Annapolis "
    },
    {
        "latLng": [
            44.330999999999996,
            -69.72999999999999
        ],
        "state": "ME",
        "name": "Augusta "
    },
    {
        "latLng": [
            42.708999999999996,
            -84.554
        ],
        "state": "MI",
        "name": "Lansing "
    },
    {
        "latLng": [
            44.948,
            -93.104
        ],
        "state": "MN",
        "name": "St. Paul "
    },
    {
        "latLng": [
            38.571999999999996,
            -92.19
        ],
        "state": "MO",
        "name": "Jefferson City "
    },
    {
        "latLng": [
            32.321,
            -90.208
        ],
        "state": "MS",
        "name": "Jackson "
    },
    {
        "latLng": [
            46.597,
            -112.02
        ],
        "state": "MT",
        "name": "Helena "
    },
    {
        "latLng": [
            35.821999999999996,
            -78.65899999999999
        ],
        "state": "NC",
        "name": "Raleigh "
    },
    {
        "latLng": [
            46.805,
            -100.767
        ],
        "state": "ND",
        "name": "Bismarck "
    },
    {
        "latLng": [
            40.815999999999995,
            -96.688
        ],
        "state": "NE",
        "name": "Lincoln "
    },
    {
        "latLng": [
            43.232,
            -71.56
        ],
        "state": "NH",
        "name": "Concord "
    },
    {
        "latLng": [
            40.223,
            -74.764
        ],
        "state": "NJ",
        "name": "Trenton "
    },
    {
        "latLng": [
            35.678999999999995,
            -105.954
        ],
        "state": "NM",
        "name": "Santa Fe "
    },
    {
        "latLng": [
            39.147999999999996,
            -119.743
        ],
        "state": "NV",
        "name": "Carson City"
    },
    {
        "latLng": [
            42.666,
            -73.79899999999999
        ],
        "state": "NY",
        "name": "Albany "
    },
    {
        "latLng": [
            39.989,
            -82.987
        ],
        "state": "OH",
        "name": "Columbus "
    },
    {
        "latLng": [
            35.467,
            -97.51299999999999
        ],
        "state": "OK",
        "name": "Oklahoma City "
    },
    {
        "latLng": [
            44.925,
            -123.02199999999999
        ],
        "state": "OR",
        "name": "Salem "
    },
    {
        "latLng": [
            40.275999999999996,
            -76.88499999999999
        ],
        "state": "PA",
        "name": "Harrisburg "
    },
    {
        "latLng": [
            41.821999999999996,
            -71.42
        ],
        "state": "RI",
        "name": "Providence "
    },
    {
        "latLng": [
            34.039,
            -80.886
        ],
        "state": "SC",
        "name": "Columbia "
    },
    {
        "latLng": [
            44.373,
            -100.32199999999999
        ],
        "state": "SD",
        "name": "Pierre "
    },
    {
        "latLng": [
            36.172,
            -86.785
        ],
        "state": "TN",
        "name": "Nashville-Davidson"
    },
    {
        "latLng": [
            30.305999999999997,
            -97.75099999999999
        ],
        "state": "TX",
        "name": "Austin "
    },
    {
        "latLng": [
            40.777,
            -111.92999999999999
        ],
        "state": "UT",
        "name": "Salt Lake City "
    },
    {
        "latLng": [
            37.531,
            -77.475
        ],
        "state": "VA",
        "name": "Richmond "
    },
    {
        "latLng": [
            44.266,
            -72.572
        ],
        "state": "VT",
        "name": "Montpelier "
    },
    {
        "latLng": [
            47.041999999999994,
            -122.89399999999999
        ],
        "state": "WA",
        "name": "Olympia "
    },
    {
        "latLng": [
            43.08,
            -89.38799999999999
        ],
        "state": "WI",
        "name": "Madison "
    },
    {
        "latLng": [
            38.351,
            -81.63
        ],
        "state": "WV",
        "name": "Charleston "
    },
    {
        "latLng": [
            41.144999999999996,
            -104.792
        ],
        "state": "WY",
        "name": "Cheyenne "
    }
];

function tester(salesman) {
    for (j = 0; j < salesman.length; j++) {
        var map = $('#map').vectorMap('get', 'mapObject');
        for (i = 0; i < points.length - 1; i++) {
            console.log(points[i])
            var coords1 = map.latLngToPoint(markerArray[points[i]].latLng[0], markerArray[points[i]].latLng[1]);
            var coords2 = map.latLngToPoint(markerArray[points[i + 1]].latLng[0], markerArray[points[i + 1]].latLng[1]);
        }
    }
}


function drawCoords(paths, breaks) {
    console.log("hello")
    var map = $('#map').vectorMap('get', 'mapObject');
    $('#svgMapOverlay').empty()
    var draw = SVG('svgMapOverlay').size(660, 400);
    var depot1= paths.slice(0,breaks[0]);
    var depot2= paths.slice(breaks[0],breaks[1]);
    var depot3= paths.slice(breaks[1],paths.length);


    drawPoints(depot1, map, draw);
    drawPoints(depot2, map, draw);
    drawPoints(depot3, map, draw);
}

function drawPoints(points, map, draw) {
    for (i = 0; i < points.length; i++) {
        var point1 = points[i];
        var point2 = (i < points.length - 1 ) ? points[i + 1] : points[0];
        var coords1 = map.latLngToPoint(markerArray[point1].latLng[0], markerArray[point1].latLng[1]);
        var coords2 = map.latLngToPoint(markerArray[point2].latLng[0], markerArray[point2].latLng[1]);
        draw
            .path()
            .attr({fill: 'none', stroke: '#c00', 'stroke-width': 2})
            .M(coords1.x, coords1.y)
            .L(coords2.x, coords2.y);

    }
}

function initializeMap() {
    var map = $('#map').vectorMap({
        map: 'us_aea_en',
        zoomMin: 1,
        zoomMax: 1,
        markers: markerArray,
        markerStyle: {
            initial: {"r": 3}
        }
    });

    var map = $('#map').vectorMap('get', 'mapObject');
    var draw = SVG('svgMapOverlay').size(660, 400);


//let's do another!
    var coords1 = map.latLngToPoint(markerArray[1].latLng[0], markerArray[1].latLng[1]);
    var coords2 = map.latLngToPoint(markerArray[2].latLng[0], markerArray[2].latLng[1]);
    draw
        .path()
        .attr({fill: 'none', stroke: '#c00', 'stroke-width': 2})
        .M(coords1.x, coords1.y)
        .L(coords2.x, coords2.y);


} // end Jquery call