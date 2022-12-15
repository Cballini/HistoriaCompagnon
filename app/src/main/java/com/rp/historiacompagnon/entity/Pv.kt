package com.rp.historiacompagnon.entity

data class Pv(
    var max: Int,
    var current: Int,
    var bonus: Int,
    var increaseLevel: String
) {

    constructor() : this(0, 0, 0, "")
}
