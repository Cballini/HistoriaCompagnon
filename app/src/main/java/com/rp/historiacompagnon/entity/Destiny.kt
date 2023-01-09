package com.rp.historiacompagnon.entity

data class Destiny(
    var name: String,
    var value: String,
    var ideal: String,
    var link: String,
    var defect: String,
    var trait: String
) {
    constructor() : this("", "", "", "", "", "")
}