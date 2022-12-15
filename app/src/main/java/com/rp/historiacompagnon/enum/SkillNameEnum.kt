package com.rp.historiacompagnon.enum

enum class SkillNameEnum(val value: String, val attribute: CharacteristicEnum) {
    ACROBATICS("Acrobaties", CharacteristicEnum.DEXTERITY),
    ARCANA("Arcanes", CharacteristicEnum.INTELLECT),
    ATHLETICS("Athlétisme", CharacteristicEnum.STRENGTH),
    DISCRETION("Discrétion", CharacteristicEnum.DEXTERITY),
    DRESSAGE("Dressage", CharacteristicEnum.WISDOM),
    SNEAKING("Escamotage", CharacteristicEnum.DEXTERITY),
    HISTORY("Histoire", CharacteristicEnum.INTELLECT),
    INTIMIDATION("Intimidation", CharacteristicEnum.CHARISMA),
    INVESTIGATION("Investigation", CharacteristicEnum.INTELLECT),
    MEDICINE("Médecine", CharacteristicEnum.WISDOM),
    NATURE("Nature", CharacteristicEnum.INTELLECT),
    PERCEPTION("Perception", CharacteristicEnum.WISDOM),
    INSIGHT("Perspicacité", CharacteristicEnum.WISDOM),
    PERSUASION("Persuasion", CharacteristicEnum.CHARISMA),
    RELIGION("Religion", CharacteristicEnum.INTELLECT),
    REPRESENTATION("Représentation", CharacteristicEnum.CHARISMA),
    TRICKERY("Surpercherie", CharacteristicEnum.CHARISMA),
    SURVIVAL("Survie", CharacteristicEnum.WISDOM),
    DEFAULT("", CharacteristicEnum.STRENGTH);

    companion object {
        @JvmStatic
        fun getMapSkill(): HashMap<String, CharacteristicEnum> {
            var map = HashMap<String, CharacteristicEnum>()
            map[ACROBATICS.value] = ACROBATICS.attribute
            map[ARCANA.value] = ARCANA.attribute
            map[ATHLETICS.value] = ATHLETICS.attribute
            map[DISCRETION.value] = DISCRETION.attribute
            map[DRESSAGE.value] = DRESSAGE.attribute
            map[SNEAKING.value] = SNEAKING.attribute
            map[HISTORY.value] = HISTORY.attribute
            map[INTIMIDATION.value] = INTIMIDATION.attribute
            map[INVESTIGATION.value] = INVESTIGATION.attribute
            map[MEDICINE.value] = MEDICINE.attribute
            map[NATURE.value] = NATURE.attribute
            map[PERCEPTION.value] = PERCEPTION.attribute
            map[INSIGHT.value] = INSIGHT.attribute
            map[PERSUASION.value] = PERSUASION.attribute
            map[RELIGION.value] = RELIGION.attribute
            map[REPRESENTATION.value] = REPRESENTATION.attribute
            map[TRICKERY.value] = TRICKERY.attribute
            map[SURVIVAL.value] = SURVIVAL.attribute
            return map
        }

        @JvmStatic
        fun getListStringSkill(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(ACROBATICS.value)
            list.add(ARCANA.value)
            list.add(ATHLETICS.value)
            list.add(DISCRETION.value)
            list.add(DRESSAGE.value)
            list.add(SNEAKING.value)
            list.add(HISTORY.value)
            list.add(INTIMIDATION.value)
            list.add(INVESTIGATION.value)
            list.add(MEDICINE.value)
            list.add(NATURE.value)
            list.add(PERCEPTION.value)
            list.add(INSIGHT.value)
            list.add(PERSUASION.value)
            list.add(RELIGION.value)
            list.add(REPRESENTATION.value)
            list.add(TRICKERY.value)
            list.add(SURVIVAL.value)
            return list
        }
    }
}