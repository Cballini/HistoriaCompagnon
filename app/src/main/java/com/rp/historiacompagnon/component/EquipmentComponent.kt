package com.rp.historiacompagnon.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.rp.historiacompagnon.R

class EquipmentComponent@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var equipmentTypeIcon: ImageView
    var equipmentName: TextView
    var equipmentDamageCa: TextView
    var equipmentProperties: TextView
    var equipmentExpend: ImageView
    var equipmentEdit: ImageView
    var equipmentUnfold: ConstraintLayout
    var equipmentSpecial: TextView
    var equipmentDescription: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_equipment, this, true)

        equipmentTypeIcon = findViewById(R.id.equipment_type_icon)
        equipmentName = findViewById(R.id.equipment_name)
        equipmentDamageCa = findViewById(R.id.equipment_damage_ca)
        equipmentProperties = findViewById(R.id.equipment_properties)
        equipmentExpend = findViewById(R.id.equipment_expand)
        equipmentEdit = findViewById(R.id.equipment_edit)
        equipmentUnfold = findViewById(R.id.equipment_unfolded)
        equipmentSpecial = findViewById(R.id.equipment_special_value)
        equipmentDescription = findViewById(R.id.equipment_description_value)
    }
}