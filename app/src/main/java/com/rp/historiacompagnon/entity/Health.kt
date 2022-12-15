package com.rp.historiacompagnon.entity

data class Health(
    var pv: Pv,
    var lifeDices: ArrayList<LifeDice>,
    var exhaustion: Int
) {

    constructor() : this(Pv(), ArrayList(), 0)
}