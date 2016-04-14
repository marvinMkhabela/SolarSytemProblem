function getDestination() {
    var destination = document.getElementById('DestinationDropDown').value;
    window.location = '/selectedDestination?selectedDestination=' + destination;
}