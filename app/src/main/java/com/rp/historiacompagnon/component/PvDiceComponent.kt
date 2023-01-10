package com.rp.historiacompagnon.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.rp.historiacompagnon.R

class PvDiceComponent@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var pvDiceTitle: TextView
    var pvDiceValue: TextView
    var pvDiceSeparator: TextView
    var pvDiceMaxValue: TextView
    var pvDiceBonus: TextView

    var pvDiceAdd: ImageView
    var pvDiceMinus: ImageView
    var pvDiceEdit: ImageView


    init {
        LayoutInflater.from(context).inflate(R.layout.component_pv_dice, this, true)

        pvDiceTitle = findViewById(R.id.fight_pv_dice_title)
        pvDiceValue = findViewById(R.id.fight_pv_dice_value)
        pvDiceSeparator = findViewById(R.id.fight_pv_dice_separator)
        pvDiceMaxValue = findViewById(R.id.fight_pv_dice_max_value)
        pvDiceBonus = findViewById(R.id.fight_pv_dice_bonus_value)
        pvDiceAdd = findViewById(R.id.fight_pv_dice_add)
        pvDiceMinus = findViewById(R.id.fight_pv_dice_minus)
        pvDiceEdit = findViewById(R.id.fight_pv_dice_edit)
    }
}