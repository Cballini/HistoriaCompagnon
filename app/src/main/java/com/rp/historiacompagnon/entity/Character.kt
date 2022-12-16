package com.rp.historiacompagnon.entity

data class Character(
    var familyName: String,
    var name: String,
    var age: Int,
    var size: Int,
    var weight: Int,
    var inspirationPoints: Int,
    var gloryPoints: Int,
    var description: String,
    var race: Race,
    var aptitudes: ArrayList<Aptitude>,
    var health: Health,
    var characteristics: ArrayList<Characteristic>,
    var skills: ArrayList<Skill>,
    var destiny: Destiny,
    var inventory: Inventory, // inventaire contient equipement equiped
    var career: Career,
    var relationships: ArrayList<Pnj>,
    var jobs: ArrayList<Job>,
    var player: String, // user.key
    var team: String, // team.key
    var key: String
) {
    constructor() : this(
        "",
        "",
        0,
        0,
        0,
        0,
        0,
        "",
        Race(),
        ArrayList(),
        Health(),
        ArrayList(),
        ArrayList(),
        Destiny(),
        Inventory(),
        Career(),
        ArrayList(),
        ArrayList(),
        "",
        "",
        ""
    )

    fun concatFullName(): String = this.name + ' ' + this.familyName
}