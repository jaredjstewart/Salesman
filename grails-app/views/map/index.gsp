<!DOCTYPE html>
<html>
<head>
    <title>2 planets</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <asset:stylesheet href="map.css"/>
    <asset:javascript src="map.js"/>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="container" style="width:690px;">
    <div class="row" style="padding-bottom: 20px">
        <div class="col-xs-12" style="margin-left: -10px;">
            <div id="mapContainer">
                <div id="svgMapOverlay"></div>
                <div id="map"></div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-6">
            <p>
            <button type="button" class="btn btn-default" onclick="start()">Draw paths!</button>
                <button type="button" class="btn btn-default" onclick="drawRouteWithBreaks(${[]}, ${[]})">Stop updating</button>
            </p>
            </div>
        <div class="col-xs-3" style="text-align: right;">
            <p id="distance"></p>

        </div>
        <div class ="col-xs-3"  style="text-align: right;">
            <p id ="iteration"></p>
        </div>
    </div>

</div>
<script type="text/javascript">
    $(document).ready(function () {
        initializeMap();
    });
</script>
</body>
</html>
