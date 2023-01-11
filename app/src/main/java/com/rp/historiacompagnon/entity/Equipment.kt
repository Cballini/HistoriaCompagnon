package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.EquipmentPropertyEnum
import com.rp.historiacompagnon.enum.EquipmentTypeEnum
import java.util.Properties

data class Equipment(
    override var name: String,
    override var description: String,
    override var equiped: Boolean,
    var damage: String,
    var type: EquipmentTypeEnum,
    /*var property: ArrayList<EquipmentPropertyEnum>,*/ // TODO voir réfacto
    var properties: String,
    var ca: Int,
    var special: String
) : Stuff(name, description, equiped) {

    constructor() : this("", "", false, "", EquipmentTypeEnum.OTHER, "", 0, "")
}