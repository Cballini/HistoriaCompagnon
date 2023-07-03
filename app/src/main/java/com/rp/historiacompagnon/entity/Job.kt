package com.rp.historiacompagnon.entity

data class Job(
    var name: String,
    var specialty: String,
    var level: Int,
    var modifier: Int,
    var lifeDiceByLvl: String,
    var typeArmor: String,
    var typeWeapon: String,
    var save: String,
    var toolMasteries: String
) {
    constructor() : this("", "", 0, 0, "", "", "", "", "")
}