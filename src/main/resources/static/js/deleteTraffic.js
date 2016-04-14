function deleteTraffic() {
    var trafficId = document.getElementById('deleteTrafficDropDown').value;
    window.location = '/deleteConfirmedTraffic?trafficId=' + trafficId;
}