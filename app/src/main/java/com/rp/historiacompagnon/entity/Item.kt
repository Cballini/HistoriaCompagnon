package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.ItemTypeEnum

data class Item(
    var name: String,
    var description: String,
    var type: ItemTypeEnum,
    var equiped: Boolean
) {
    constructor(): this("", "", ItemTypeEnum.ITEM, false)
}