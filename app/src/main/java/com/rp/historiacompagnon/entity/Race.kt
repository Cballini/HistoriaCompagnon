package com.rp.historiacompagnon.entity

class Race(var familia :String,
           var familiaInstinct :String,
           var espece :String,
           var especeInstinct :String,
           var vitesse :String) {

    // TODO Voir si ajout id

    constructor(): this("","","","","")
}