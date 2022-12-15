package com.rp.historiacompagnon.enum

enum class RelationshipTypeEnum(var value: String) {
    NEUTRAL("Neutre"),
    AMICAL("Amical"),
    LOVER("Amant/amoureux"),
    FAMILY("Membre de la famille"),
    OPPONENT("Opposant"),
    RIVAL("Rival");

    companion object {
        @JvmStatic
        fun getListStringItemType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(NEUTRAL.value)
            list.add(AMICAL.value)
            list.add(LOVER.value)
            list.add(FAMILY.value)
            list.add(OPPONENT.value)
            list.add(RIVAL.value)
            return list
        }
    }
}