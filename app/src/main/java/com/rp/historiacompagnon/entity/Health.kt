package com.rp.historiacompagnon.entity

data class Health(
    var pv: Pv,
    var lifeDices: ArrayList<LifeDice>
) {
    var exhaustion: Int = 0 // TODO gestion ?

    constructor() : this(Pv(), ArrayList())
}