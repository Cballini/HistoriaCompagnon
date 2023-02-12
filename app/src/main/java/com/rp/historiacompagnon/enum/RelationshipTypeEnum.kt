package com.rp.historiacompagnon.enum

import android.content.Context
import com.rp.historiacompagnon.R

enum class RelationshipTypeEnum(var value: String) {
    NEUTRAL("Neutre"),
    AMICAL("Amical"),
    LOVER("Amant/amoureux"),
    FAMILY("Membre de la famille"),
    OPPONENT("Opposant"),
    RIVAL("Rival");

    companion object {
        @JvmStatic
        fun getListStringRelashionshipType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(LOVER.value)
            list.add(AMICAL.value)
            list.add(FAMILY.value)
            list.add(NEUTRAL.value)
            list.add(RIVAL.value)
            list.add(OPPONENT.value)
            return list
        }

        @JvmStatic
        fun getListStringRelashionshipTypeForFilter(context: Context): ArrayList<String> {
            val first = context.getString(R.string.first)
            var list = ArrayList<String>()
            list.add(LOVER.value + first)
            list.add(AMICAL.value + first)
            list.add(FAMILY.value + first)
            list.add(NEUTRAL.value + first)
            list.add(RIVAL.value + first)
            list.add(OPPONENT.value + first)
            return list
        }

        @JvmStatic
        fun getTypeByValue(value: String): RelationshipTypeEnum {
            var type = NEUTRAL
            when (value) {
                LOVER.value -> type = LOVER
                AMICAL.value -> type = AMICAL
                FAMILY.value -> type = FAMILY
                OPPONENT.value -> type = OPPONENT
                RIVAL.value -> type = RIVAL
            }
            return type
        }
    }
}