<!DOCTYPE html>
<html>
<head>
    <title>2 planets</title>
    <asset:stylesheet href="map.css"/>
    <asset:javascript src="map.js"/>
</head>

<body>
<button type="button" onclick="drawCoords(${model.points}, ${model.breaks})">Draw paths!</button>

<button type="button" onclick="pollForUpdates()">Update paths!</button>


<button type="button" onclick="drawCoords(${[]}, ${[]})">Reset</button>
%{--<button type="button" onclick="drawCoords(${(0..50).toList()}, ${[5, 10]})">Reset</button>--}%
<div id="mapContainer">

    <div id="svgMapOverlay"></div>

    <div id="map"></div>

</div>
<script type="text/javascript">
$(document).ready( function() {
 initializeMap();
});
</script>
</body>
</html>
