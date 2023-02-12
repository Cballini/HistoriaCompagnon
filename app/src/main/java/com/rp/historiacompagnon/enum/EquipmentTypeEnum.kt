package com.rp.historiacompagnon.enum

import android.content.Context
import com.rp.historiacompagnon.R

enum class EquipmentTypeEnum(val value: String) {
    WEAPON("Arme"),
    WEAPON_RANGE("Distance"),
    ARMOR("Armure"),
    SHIELD("Bouclier"),
    GUN("Arme à feu"),
    OTHER("Accessoire");

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

        @JvmStatic
        fun getListStringEquipmentForFilter(context: Context): ArrayList<String> {
            val first = context.getString(R.string.first)
            var list = ArrayList<String>()
            list.add(WEAPON.value + first)
            list.add(WEAPON_RANGE.value + first)
            list.add(ARMOR.value + first)
            list.add(SHIELD.value + first)
            list.add(GUN.value + first)
            list.add(OTHER.value + first)
            return list
        }
    }
}