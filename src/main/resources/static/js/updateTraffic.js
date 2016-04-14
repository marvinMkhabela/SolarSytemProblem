function updateTraffic() {

    document.getElementById('updateDetails').innerHTML =
        '<form id="updateForm">' +
        'New origin: <input id="origin" type="text"/>' +
        'New destination: <input id="destination" type="text"/>' +
        'New traffic delay: <input id="delay" type="text">' +
        '</form>' +
        '<button type="button" onclick="updateTrafficDetails()">Submit</button>';
}

function updateTrafficDetails() {

    var trafficId = document.getElementById('trafficDropDown').value;
    var origin = document.getElementById('origin').value;
    var destination = document.getElementById('destination').value;
    var delay = document.getElementById('delay').value;

    if ((origin.length != 0) && (destination.length != 0) && (delay.length != 0)) {
        window.location = '/confirmedTrafficUpdate?trafficId=' + trafficId +
            '&origin=' + origin + '&destination=' + destination + '&delay=' + delay;
    }
    else {
        document.getElementById('errorMessages').innerHTML =
            '<p>Please Fill in all fields before submitting</p>';
    }
}