package com.rp.historiacompagnon.util

import kotlin.random.Random


object Utils {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    // TODO vérif donne la même valeur plusieurs faois d'affilé
    @JvmStatic
    fun randomStringByKotlinRandom() = (1..12)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}