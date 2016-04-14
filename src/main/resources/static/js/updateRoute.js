function updateRoute() {

    var division = document.getElementById('routeDetails');
    division.innerHTML =
        '<form id="updateRouteForm">' +
        'New origin: <input id="newOrigin" type="text"/>' +
        'New destination: <input id="newDestination" type="text"/>' +
        'New Distance: <input id="newDistance" type="text">' +
        '</form>' +
        '<button type="button" onclick="confirmRouteUpdate()">Submit</button>';
}

function confirmRouteUpdate() {

    var selectedId = document.getElementById('updateRouteDropDown').value;
    var newOrigin = document.getElementById("newOrigin").value;
    var newDestination = document.getElementById("newDestination").value;
    var newDistance = document.getElementById("newDistance").value;

    if ((selectedId != "null") && (newOrigin.length != 0) && (newDestination.length != 0)
        && (newDistance.length != 0)) {
        window.location = '/confirmRouteUpdate?selectedId=' + selectedId
            + '&origin=' + newOrigin + '&destination=' + newDestination
            + '&distance=' + newDistance;
    }
    else {
        document.getElementById('errorMessages').innerHTML =
            '<p>Please complete all fields before submitting</p>';
    }
}
