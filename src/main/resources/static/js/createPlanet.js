function checkValues() {

    var node = document.getElementById("createdNode").value;
    var name = document.getElementById("createdName").value;

    if (node.length != 0 && name.length != 0) {
        $.get('/createPlanet?creationNode=' + node + '&creationName=' + name,
            function (data, httpStatus) {
                if (data == "No") {
                    window.location.href = '/persistEntry?creationNode=' + node + '&creationName=' + name;
                }
                else {
                    document.getElementById("errorMessages").innerHTML = 'Oops, there was a clash in the database';
                }
            });
    }
    else {
        var e = document.getElementById("errorMessages");
        e.innerHTML = '<p>Please complete all fields before clicking submit</p>';
    }
}