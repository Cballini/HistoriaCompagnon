package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.EquipmentTypeEnum

data class Character(
    var familyName: String,
    var name: String,
    var age: Int,
    var size: Int,
    var weight: Int,
    var speed: String,
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
        "",
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
    ) {
        // TODO modif après création inventaire
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.WEAPON, "", 0, ""))
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.WEAPON, "", 0, ""))
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.SHIELD, "", 0, ""))
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.ARMOR, "", 0, ""))
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.OTHER, "", 0, ""))
        inventory.equipments.add(Equipment("", "", true, "", EquipmentTypeEnum.OTHER, "", 0, ""))
    }

    fun concatFullName(): String = this.name + ' ' + this.familyName
}