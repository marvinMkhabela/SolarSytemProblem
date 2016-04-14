function createRoute() {
    var origin = document.getElementById('originDropDown').value;
    var destination = document.getElementById('destinationDropDown').value;
    var distance = document.getElementById('distance').value;

    if ((origin != "null") && (destination != "null") && (distance.length != 0)) {
        $.get('/proposeRouteCreation?origin=' + origin + '&destination=' + destination,
            function (data, httpStatus) {
                if (data == "No") {
                    window.location = '/createConfirmedRoute?origin=' + origin + '&destination=' + destination
                        + '&distance=' + distance;
                }
                else {
                    document.getElementById('errorMessages').innerHTML =
                        '<p>Route already exists!</p>';
                }
            });
    }
    else {
        document.getElementById('errorMessages').innerHTML =
            '<p>Please fill in all fields before clicking submit</p>';
    }
}