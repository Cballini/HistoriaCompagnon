package com.rp.historiacompagnon.enum

import android.content.Context
import com.rp.historiacompagnon.R

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

        @JvmStatic
        fun getListStringAptitudeTypeForFilter(context: Context): ArrayList<String> {
            val first = context.getString(R.string.first)
            var list = ArrayList<String>()
            list.add(ACTION.value + first)
            list.add(BONUS_ACTION.value + first)
            list.add(REACTION.value + first)
            list.add(PASSIVE.value + first)
            list.add(AVANTAGE.value + first)
            list.add(DESAVANTAGE.value + first)
            list.add(SPELL.value + first)
            list.add(OTHER.value + first)
            return list
        }

        @JvmStatic
        fun getTypeByValue(value: String): AptitudeTypeEnum {
            var type = OTHER
            when (value) {
                ACTION.value -> type = ACTION
                BONUS_ACTION.value -> type = BONUS_ACTION
                REACTION.value -> type = REACTION
                PASSIVE.value -> type = PASSIVE
                AVANTAGE.value -> type = AVANTAGE
                DESAVANTAGE.value -> type = DESAVANTAGE
                SPELL.value -> type = SPELL
            }
            return type
        }
    }
}