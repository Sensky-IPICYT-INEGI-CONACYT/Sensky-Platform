let map;

let iconLength = 12;
let markerIcon = L.Icon.extend({
    options: {
        iconSize: [iconLength, iconLength],
        iconAnchor: [iconLength / 2, iconLength / 2],
        popupAnchor: [0, -10]
    }
});

let markersLayer;

let markerIcons = [
    new markerIcon({iconUrl: 'http://youilab.ipicyt.edu.mx:8080/lameramera/img/icons/danger-level-1.png'}),
    new markerIcon({iconUrl: 'http://youilab.ipicyt.edu.mx:8080/lameramera/img/icons/danger-level-2.png'}),
    new markerIcon({iconUrl: 'http://youilab.ipicyt.edu.mx:8080/lameramera/img/icons/danger-level-3.png'}),
    new markerIcon({iconUrl: 'http://youilab.ipicyt.edu.mx:8080/lameramera/img/icons/danger-level-4.png'})
];

function setupMap(){
    map = L.map('map',{ zoomControl: false}).setView([24.287858,-101.2265781], 6);
    L.control.zoom({
        position: 'topright'
    }).addTo(map);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    markersLayer = L.layerGroup().addTo(map);
}
function clearMarkers() {
    markersLayer.clearLayers();
}

function addMapMarker(position, iconIndex){
    let markerIcon = markerIcons[iconIndex - 1];
    return L.marker(position, {icon: markerIcon}).addTo(markersLayer);
}
