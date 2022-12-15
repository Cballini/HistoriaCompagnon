package com.rp.historiacompagnon.enum

enum class ItemTypeEnum(val value: String) {
    // TODO enrichir
    CONSUMABLE("Consomable"),
    TOOL("Outil"),
    POTION("Potion"),
    SCROLL("Parchemin"),
    ITEM("Objet");

    companion object {
        @JvmStatic
        fun getListStringItemType(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(CONSUMABLE.value)
            list.add(TOOL.value)
            list.add(POTION.value)
            list.add(SCROLL.value)
            list.add(ITEM.value)
            return list
        }
    }
}
