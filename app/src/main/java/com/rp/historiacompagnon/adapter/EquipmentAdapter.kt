package com.rp.historiacompagnon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.entity.Equipment
import com.rp.historiacompagnon.enum.EquipmentTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewEquipmenetClickListener

class EquipmentAdapter(
    var mDataset: MutableList<Equipment>,
    callback: RecyclerViewEquipmenetClickListener,
    context: Context
) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {
    private var callback = callback
    private var contexte = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_equipment, parent, false) as ConstraintLayout

        return ViewHolder(item)
    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipment = mDataset[position]

        holder.lineObject.findViewById<ImageView>(R.id.equipment_type_icon)
            .setImageResource(getImageByEquipmentType(equipment.type))
        holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_unfolded).visibility =
            View.GONE
        holder.lineObject.findViewById<ImageView>(R.id.equipment_expand)
            .setImageResource(R.drawable.expand_more)

        holder.lineObject.findViewById<TextView>(R.id.equipment_name).text = equipment.name

        if (equipment.type == EquipmentTypeEnum.WEAPON ||
            equipment.type == EquipmentTypeEnum.WEAPON_RANGE ||
            equipment.type == EquipmentTypeEnum.GUN) {
            holder.lineObject.findViewById<TextView>(R.id.equipment_damage_ca).text =
                contexte.getString(R.string.equipment_x_damage, equipment.damage)
        } else if (equipment.type == EquipmentTypeEnum.SHIELD ||
            equipment.type == EquipmentTypeEnum.ARMOR || equipment.type == EquipmentTypeEnum.OTHER) { // TODO voir si on garde other
            holder.lineObject.findViewById<TextView>(R.id.equipment_damage_ca).text = contexte.getString(R.string.equipment_x_ca, equipment.ca.toString())
        }

        holder.lineObject.findViewById<TextView>(R.id.equipment_properties).text = equipment.properties

        if (equipment.special.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_special_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_special_layout).visibility = View.VISIBLE
            holder.lineObject.findViewById<TextView>(R.id.equipment_special_value).text = equipment.special
        }

        if (equipment.description.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_description_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_description_layout).visibility = View.VISIBLE
            holder.lineObject.findViewById<TextView>(R.id.equipment_description_value).text = equipment.description
        }

        holder.lineObject.findViewById<ImageView>(R.id.equipment_expand).setOnClickListener {
            if(holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_unfolded).visibility == View.GONE) {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_unfolded).visibility = View.VISIBLE
                holder.lineObject.findViewById<ImageView>(R.id.equipment_expand).setImageResource(R.drawable.expand_less)
            } else {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.equipment_unfolded).visibility = View.GONE
                holder.lineObject.findViewById<ImageView>(R.id.equipment_expand).setImageResource(R.drawable.expand_more)
            }
        }
        holder.lineObject.findViewById<ImageView>(R.id.equipment_delete).setOnClickListener {
            callback.onItemClicked(equipment, R.id.equipment_delete)
        }
        holder.lineObject.findViewById<ImageView>(R.id.equipment_edit).setOnClickListener {
            callback.onItemClicked(equipment, R.id.equipment_edit)
        }
    }

    private fun getImageByEquipmentType(equipmentType: EquipmentTypeEnum): Int {
        return when (equipmentType) {
            EquipmentTypeEnum.WEAPON -> R.drawable.sword
            EquipmentTypeEnum.WEAPON_RANGE -> R.drawable.target
            EquipmentTypeEnum.GUN -> R.drawable.gun
            EquipmentTypeEnum.SHIELD -> R.drawable.shield
            EquipmentTypeEnum.ARMOR -> R.drawable.armor
            EquipmentTypeEnum.OTHER -> R.drawable.jewel
        }
    }

    class ViewHolder(val lineObject: ConstraintLayout) : RecyclerView.ViewHolder(lineObject)
}