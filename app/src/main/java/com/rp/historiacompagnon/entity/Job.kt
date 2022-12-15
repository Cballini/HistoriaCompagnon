package com.rp.historiacompagnon.entity

data class Job(var name: String, var level: Int, var modifier: Int) {
    constructor() : this("", 0, 0)
}