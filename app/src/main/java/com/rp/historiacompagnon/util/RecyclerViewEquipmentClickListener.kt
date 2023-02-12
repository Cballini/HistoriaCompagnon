package com.rp.historiacompagnon.util

import com.rp.historiacompagnon.entity.Equipment

interface RecyclerViewEquipmentClickListener {
    fun onEquipmentClicked(equipment: Equipment, position: Int, id: Int)
}