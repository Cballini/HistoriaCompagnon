package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.ItemTypeEnum

data class Item (
    override var name: String,
    override var description: String,
    override var equiped: Boolean,
    var type: ItemTypeEnum,
    var quantity: Int
) : Stuff (name, description, equiped){
    constructor(): this("", "", false, ItemTypeEnum.ITEM, 0)
}