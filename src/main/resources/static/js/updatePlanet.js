function updateConfirmed() {
    var nodeID = document.getElementById('updateNodeDropDown').value;
    $.get('/updateNode?updateNode=' + nodeID, function (data, httpStatus) {
        var e = document.getElementById('namingForm');
        e.innerHTML = '<h2>Updating, ' + data + '</h2><form id="newNameForm">New name: ' +
            '<input id="newName" type="text"></form> <button type="button" onclick="postName()">Submit</button>';
    });
}

function postName() {
    var new_name = document.getElementById('newName').value;
    var node = document.getElementById('updateNodeDropDown').value;
    window.location = '/updateNewName?updatedName=' + new_name + '&updateNode=' + node;
}

