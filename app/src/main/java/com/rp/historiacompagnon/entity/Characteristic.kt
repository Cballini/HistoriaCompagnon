package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.CharacteristicEnum

class Characteristic(var name: CharacteristicEnum, var value: Int) {

    constructor(): this(CharacteristicEnum.STRENGTH, 0)
}