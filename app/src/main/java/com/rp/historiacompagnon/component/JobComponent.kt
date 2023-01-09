package com.rp.historiacompagnon.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.rp.historiacompagnon.R

class JobComponent@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var jobName: TextView
    var jobSpecialityLvl: TextView
    var jobModifier: TextView
    var jobLifeDiceLvl: TextView
    var jobArmors: TextView
    var jobWeapons: TextView
    var jobThrowSave: TextView
    var jobEdit: ImageView
    var jobDelete: ImageView


    init {
        LayoutInflater.from(context).inflate(R.layout.component_job, this, true)

        jobName = findViewById(R.id.job_name)
        jobSpecialityLvl = findViewById(R.id.job_specialty_level)
        jobModifier = findViewById(R.id.job_modifier_value)
        jobLifeDiceLvl = findViewById(R.id.job_life_dice_lvl_value)
        jobArmors = findViewById(R.id.job_armors_value)
        jobWeapons = findViewById(R.id.job_weapons_value)
        jobThrowSave = findViewById(R.id.job_throw_save_value)
        jobEdit = findViewById(R.id.job_edit)
        jobDelete = findViewById(R.id.job_delete)
    }
}