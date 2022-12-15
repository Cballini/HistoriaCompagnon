package com.rp.historiacompagnon.enum

enum class AptitudeTypeEnum(val value: String) {
    ACTION("Action"),
    BONUS_ACTION("Action bonus"),
    REACTION("Réaction"),
    PASSIVE("Passif"),
    AVANTAGE("Avantage"),
    DESAVANTAGE("Désavantage"),
    SPELL("Sort"),
    OTHER("Autre");

    companion object{
        @JvmStatic
        fun getListStringAptitudeType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(ACTION.value)
            list.add(BONUS_ACTION.value)
            list.add(REACTION.value)
            list.add(PASSIVE.value)
            list.add(AVANTAGE.value)
            list.add(DESAVANTAGE.value)
            list.add(SPELL.value)
            list.add(OTHER.value)
            return list
        }
    }
}