package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.ItemTypeEnum

data class Item (
    override var name: String,
    override var description: String,
    override var equiped: Boolean,
    var type: ItemTypeEnum
) : Stuff (name, description, equiped){
    constructor(): this("", "", false, ItemTypeEnum.ITEM)
}