package com.rp.historiacompagnon.entity

data class LifeDice(var type: Int, var number: Int, var job: String) {

    constructor() : this(0, 0, "")
}
