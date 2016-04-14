function deletePlanet() {
    var planet = document.getElementById('deleteDropDown').value;
    window.location.href = '/deletePlanet?planetName=' + planet;
}