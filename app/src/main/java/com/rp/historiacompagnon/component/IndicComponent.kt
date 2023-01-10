package com.rp.historiacompagnon.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.rp.historiacompagnon.R

class IndicComponent@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var indicTitle: TextView
    var indicValue: TextView

    var indicEdit: ImageView


    init {
        LayoutInflater.from(context).inflate(R.layout.component_indic, this, true)

        indicTitle = findViewById(R.id.fight_indic_title)
        indicValue = findViewById(R.id.fight_indic_value)
        indicEdit = findViewById(R.id.fight_indic_edit)
    }
}