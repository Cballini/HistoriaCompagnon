package com.rp.historiacompagnon.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.entity.Aptitude
import com.rp.historiacompagnon.enum.AptitudeTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class AptitudeAdapter(var mDataset: MutableList<Aptitude>, callback : RecyclerViewClickListener, context: Context) : RecyclerView.Adapter<AptitudeAdapter.ViewHolder>() {
    private var callback = callback
    private var contexte = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_aptitude, parent, false) as ConstraintLayout

        return ViewHolder(item)
    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aptitude = mDataset[position]

        holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_unfolded).visibility = View.GONE
        holder.lineObject.findViewById<ImageView>(R.id.aptitude_expand).setImageResource(R.drawable.expand_more)

        holder.lineObject.findViewById<ImageView>(R.id.aptitude_type_icon).setImageResource(getImageByType(aptitude.type))
        holder.lineObject.findViewById<TextView>(R.id.aptitude_name).text = aptitude.name
        holder.lineObject.findViewById<TextView>(R.id.aptitude_short_description).text = aptitude.shortDescription

        if (aptitude.damage.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_damage_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_damage_value).text = aptitude.damage
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_damage_layout).visibility = View.VISIBLE
        }

        if (aptitude.heal.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_heal_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_heal_value).text = aptitude.heal
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_heal_layout).visibility = View.VISIBLE
        }

        if (aptitude.scope.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_scope_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_scope_value).text = aptitude.scope
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_scope_layout).visibility = View.VISIBLE
        }

        if (aptitude.duration.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_duration_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_duration_value).text = aptitude.duration
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_duration_layout).visibility = View.VISIBLE
        }

        if (aptitude.use.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_use_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_use_value).text = aptitude.use
            holder.lineObject.findViewById<TextView>(R.id.aptitude_used_value).text = aptitude.used.toString()
            holder.lineObject.findViewById<ImageView>(R.id.aptitude_add_used).setOnClickListener {
                callback.onItemClicked(position, holder.lineObject.findViewById(R.id.aptitude_add_used), R.id.aptitude_add_used)
            }
            holder.lineObject.findViewById<ImageView>(R.id.aptitude_minus_used).setOnClickListener {
                callback.onItemClicked(position, holder.lineObject.findViewById(R.id.aptitude_minus_used), R.id.aptitude_minus_used)
            }
            holder.lineObject.findViewById<ImageView>(R.id.aptitude_reset_used).setOnClickListener {
                callback.onItemClicked(position, holder.lineObject.findViewById(R.id.aptitude_reset_used), R.id.aptitude_reset_used)
            }
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_use_layout).visibility = View.VISIBLE
        }

        if (aptitude.effect.isEmpty()) {
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_effect_layout).visibility = View.GONE
        } else {
            holder.lineObject.findViewById<TextView>(R.id.aptitude_effect_value).text = aptitude.effect
            holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_effect_layout).visibility = View.VISIBLE
        }

        holder.lineObject.findViewById<TextView>(R.id.aptitude_type).text = contexte.getString(R.string.aptitude_type, aptitude.type.value)
        holder.lineObject.findViewById<TextView>(R.id.aptitude_tag).text = aptitude.tag.joinToString(separator = ", ") { it.value }

        holder.lineObject.findViewById<ImageView>(R.id.aptitude_expand).setOnClickListener {
            if(holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_unfolded).visibility == View.GONE) {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_unfolded).visibility = View.VISIBLE
                holder.lineObject.findViewById<ImageView>(R.id.aptitude_expand).setImageResource(R.drawable.expand_less)
            } else {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.aptitude_unfolded).visibility = View.GONE
                holder.lineObject.findViewById<ImageView>(R.id.aptitude_expand).setImageResource(R.drawable.expand_more)
            }
        }
        holder.lineObject.findViewById<ImageView>(R.id.aptitude_type_delete).setOnClickListener {
            callback.onItemClicked(position, holder.lineObject.findViewById(R.id.aptitude_type_delete), R.id.aptitude_type_delete)
        }
        holder.lineObject.findViewById<ImageView>(R.id.aptitude_type_edit).setOnClickListener {
            callback.onItemClicked(position, holder.lineObject.findViewById(R.id.aptitude_type_edit), R.id.aptitude_type_edit)
        }
    }

    private fun getImageByType(aptitudeType: AptitudeTypeEnum): Int {
        return when (aptitudeType) {
            AptitudeTypeEnum.ACTION -> R.drawable.action
            AptitudeTypeEnum.BONUS_ACTION -> R.drawable.bonus
            AptitudeTypeEnum.REACTION ->  R.drawable.reaction
            AptitudeTypeEnum.PASSIVE -> R.drawable.passive
            AptitudeTypeEnum.AVANTAGE -> R.drawable.avantage
            AptitudeTypeEnum.DESAVANTAGE -> R.drawable.desavantage
            AptitudeTypeEnum.SPELL -> R.drawable.spell
            AptitudeTypeEnum.OTHER -> R.drawable.other
        }
    }

    class ViewHolder(val lineObject: ConstraintLayout) : RecyclerView.ViewHolder(lineObject)
}