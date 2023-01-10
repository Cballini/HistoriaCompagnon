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

        @JvmStatic
        fun getCharacteristicModifier(value: Int): String {
            var modifier = ""
            if (value == 1) modifier = "-5"
            if (value == 2 || value == 3) modifier = "-4"
            if (value == 4 || value == 5) modifier = "-3"
            if (value == 6 || value == 7) modifier = "-2"
            if (value == 8 || value == 9) modifier = "-1"
            if (value == 10 || value == 11) modifier = "0"
            if (value == 12 || value == 13) modifier = "+1"
            if (value == 14 || value == 15) modifier = "+2"
            if (value == 16 || value == 17) modifier = "+3"
            if (value == 18 || value == 19) modifier = "+4"
            if (value == 20) modifier = "+5"
            return modifier
        }
    }
}