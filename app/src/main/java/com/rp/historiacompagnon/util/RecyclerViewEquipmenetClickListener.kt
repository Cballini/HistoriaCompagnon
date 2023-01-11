package com.rp.historiacompagnon.util

import com.rp.historiacompagnon.entity.Equipment

interface RecyclerViewEquipmenetClickListener {
    fun onItemClicked(equipment: Equipment, id: Int)
}