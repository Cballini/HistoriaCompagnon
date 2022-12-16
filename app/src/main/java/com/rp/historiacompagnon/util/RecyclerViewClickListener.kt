package com.rp.historiacompagnon.util

import android.view.View

interface RecyclerViewClickListener {
    fun onItemClicked(position: Int, v: View, id: Int)
}