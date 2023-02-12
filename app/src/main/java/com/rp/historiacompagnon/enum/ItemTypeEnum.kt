package com.rp.historiacompagnon.enum

import android.content.Context
import com.rp.historiacompagnon.R

enum class ItemTypeEnum(val value: String) {
    // TODO enrichir
    QUEST("Quête"),
    CONSUMABLE("Consomable"),
    TOOL("Outil"),
    POTION("Potion"),
    SCROLL("Parchemin"),
    ITEM("Objet");

    companion object {
        @JvmStatic
        fun getListStringItemType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(QUEST.value)
            list.add(CONSUMABLE.value)
            list.add(TOOL.value)
            list.add(POTION.value)
            list.add(SCROLL.value)
            list.add(ITEM.value)
            return list
        }

        @JvmStatic
        fun getListStringItemForFilter(context: Context): ArrayList<String> {
            val first = context.getString(R.string.first)
            var list = ArrayList<String>()
            list.add(QUEST.value + first)
            list.add(CONSUMABLE.value + first)
            list.add(TOOL.value + first)
            list.add(POTION.value + first)
            list.add(SCROLL.value + first)
            list.add(ITEM.value + first)
            return list
        }

        @JvmStatic
        fun getTypeByValue(value: String): ItemTypeEnum {
            var type = ITEM
            when (value) {
                QUEST.value -> type = QUEST
                CONSUMABLE.value -> type = CONSUMABLE
                TOOL.value -> type = TOOL
                POTION.value -> type = POTION
                SCROLL.value -> type = SCROLL
            }
            return type
        }
    }
}
