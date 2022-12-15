package com.rp.historiacompagnon.entity

data class LifeDice(var type: String, var number: Int, var job: String) {

    constructor() : this("", 0, "")
}
