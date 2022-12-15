package com.rp.historiacompagnon.enum

enum class AptitudeTagEnum(val value: String) {
    AVENTURE("Aventure"),
    FIGHT("Combat"),
    UTILITY("Utilitaire"),
    HEAL("Soin"),
    OUT_FIGHT("Hors combat");

    companion object {
        @JvmStatic
        fun getListStringAptitudeTag(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add(AVENTURE.value)
            list.add(FIGHT.value)
            list.add(UTILITY.value)
            list.add(HEAL.value)
            list.add(OUT_FIGHT.value)
            return list
        }
    }
}