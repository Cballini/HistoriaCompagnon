package com.rp.historiacompagnon.enum

enum class EquipmentPropertyEnum(val value: String) {
    FIST("Poing armé"),
    PARADE("Parade"),
    BULKY("Encombrant"),
    SPECIAL("Spécial"),
    POLYVALENT_8("Polyvalent (1d8)"),
    POLYVALENT_10("Polyvalent (1d10)"),
    LIGHT("Légère"),
    DISSIMULATION("Dissimulation"),
    ARISTOCRATIC("Aristocratique"),
    THROW("Lancer (portée moyenne)"),
    MUNITION_SHORT("Munitions (portée courte)"),
    MUNITION_MIDDLE("Munitions (portée moyenne)"),
    MUNITION_LONG("Munitions (portée longue)"),
    FINESSE("Finesse"),
    PAIR("Paire"),
    POLEARM("Arme d'hast"),
    TWO_HANDS("A deux mains"),
    HEAVY("Lourde"),
    REACH("Allonge"),
    RELOAD("Rechargement"),
    SHRAPNEL("Shrapnel"),
    PRATICAL("Pratique");

    companion object {
        @JvmStatic
        fun getListStringEquipmentProperty(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(FIST.value)
            list.add(PARADE.value)
            list.add(BULKY.value)
            list.add(SPECIAL.value)
            list.add(POLYVALENT_8.value)
            list.add(POLYVALENT_10.value)
            list.add(LIGHT.value)
            list.add(DISSIMULATION.value)
            list.add(ARISTOCRATIC.value)
            list.add(THROW.value)
            list.add(MUNITION_SHORT.value)
            list.add(MUNITION_MIDDLE.value)
            list.add(MUNITION_LONG.value)
            list.add(FINESSE.value)
            list.add(PAIR.value)
            list.add(POLEARM.value)
            list.add(TWO_HANDS.value)
            list.add(HEAVY.value)
            list.add(REACH.value)
            list.add(RELOAD.value)
            list.add(SHRAPNEL.value)
            list.add(PRATICAL.value)
            return list
        }
    }
}