package com.rp.historiacompagnon.enum

enum class CharacteristicEnum(val value: String) {
    STRENGTH("Force"),
    DEXTERITY("Dextérité"),
    CONSTITUTION("Constitution"),
    CHARISMA("Charisme"),
    WISDOM("Sagesse"),
    INTELLECT("Intelligence");

    companion object {
        @JvmStatic
        fun getListStringCharacteristic(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(STRENGTH.value)
            list.add(DEXTERITY.value)
            list.add(CONSTITUTION.value)
            list.add(CHARISMA.value)
            list.add(WISDOM.value)
            list.add(INTELLECT.value)
            return list
        }
    }
}