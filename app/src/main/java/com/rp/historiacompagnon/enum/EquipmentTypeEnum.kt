package com.rp.historiacompagnon.enum

enum class EquipmentTypeEnum(val value: String) {
    WEAPON("Arme"),
    WEAPON_RANGE("Distance"),
    ARMOR("Armure"),
    SHIELD("Bouclier"),
    GUN("Arme à feu"),
    OTHER("Autre");

    companion object{
        @JvmStatic
        fun getListStringEquipmentType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(WEAPON.value)
            list.add(WEAPON_RANGE.value)
            list.add(ARMOR.value)
            list.add(SHIELD.value)
            list.add(GUN.value)
            return list
        }
    }
}