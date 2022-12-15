package com.rp.historiacompagnon.entity

data class Inventory(
    var money: Int,
    var equipments: ArrayList<Equipment>,
    var items: ArrayList<Item>
) {
    constructor(): this(0, ArrayList(), ArrayList())
}