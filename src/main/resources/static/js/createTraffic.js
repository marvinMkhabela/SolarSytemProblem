function createTraffic() {

    var origin = document.getElementById('trafficOriginDropDown').value;
    var destination = document.getElementById('trafficDestinationDropDown').value;
    var delay = document.getElementById('delay').value;

    if ((origin != "null") && (destination != "null") && (delay.length != 0)) {
        $.get('/proposeTrafficCreation?origin=' + origin + '&destination=' + destination,
            function (data, httpStatus) {
                if (data == "No") {
                    window.location = '/createConfirmedTraffic?origin=' + origin + '&destination=' + destination +
                        '&delay=' + delay;
                }
                else {
                    document.getElementById('errorMessages').innerHTML =
                        '<p>Traffic already exists!</p>';
                }
            });
    }
    else {
        document.getElementById('errorMessages').innerHTML =
            '<p>Please complete all fields before clicking submit</p>';
    }
}