package com.rp.historiacompagnon.entity

data class Career(var name: String, var rank: Int, var description: String) {
    constructor() : this("", 0, "")
}