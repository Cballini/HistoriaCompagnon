package com.rp.historiacompagnon.util

import com.rp.historiacompagnon.entity.Item

interface RecyclerViewItemClickListener {
    fun onStuffItemClicked(item: Item, position: Int, id: Int)
}