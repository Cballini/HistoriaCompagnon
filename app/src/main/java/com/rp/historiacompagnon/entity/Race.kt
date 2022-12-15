package com.rp.historiacompagnon.entity

data class Race(
    var familia: String, // TODO voir remplacer par une enum ?
    var familiaInstinct: String, // TODO voir automatique ?
    var species: String, // TODO voir remplacer par une enum ?
    var speciesInstinct: String, // TODO voir automatique ?
    var vitesse: String
) {

    constructor() : this("", "", "", "", "")
}