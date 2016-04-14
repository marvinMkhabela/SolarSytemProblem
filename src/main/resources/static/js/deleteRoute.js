function deleteRoute() {
    var routeId = document.getElementById('deleteRouteDropDown').value;
    window.location = '/confirmRouteDeletion?deletionId=' + routeId;
}