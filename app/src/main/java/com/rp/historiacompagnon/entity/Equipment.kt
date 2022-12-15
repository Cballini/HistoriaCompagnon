package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.EquipmentPropertyEnum
import com.rp.historiacompagnon.enum.EquipmentTypeEnum

data class Equipment(
    var name: String,
    var damage: String,
    var type: EquipmentTypeEnum,
    var property: ArrayList<EquipmentPropertyEnum>,
    var ca: String,
    var equiped: Boolean,
    var special: String
) {

    constructor() : this("", "", EquipmentTypeEnum.OTHER, ArrayList(), "", false, "")
}