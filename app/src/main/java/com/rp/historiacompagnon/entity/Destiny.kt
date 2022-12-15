package com.rp.historiacompagnon.entity

data class Destiny(
    var value: String,
    var trait: String,
    var ideal: String,
    var link: String
) {
    constructor() : this("", "", "", "")
}