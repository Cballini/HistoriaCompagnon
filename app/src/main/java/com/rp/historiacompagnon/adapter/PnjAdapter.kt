package com.rp.historiacompagnon.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.entity.Pnj
import com.rp.historiacompagnon.enum.RelationshipTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class PnjAdapter(
    var mDataset: MutableList<Pnj>,
    callback: RecyclerViewClickListener,
    context: Context
) : RecyclerView.Adapter<PnjAdapter.ViewHolder>() {
    private var callback = callback
    private var context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PnjAdapter.ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_pnj, parent, false) as ConstraintLayout

        return PnjAdapter.ViewHolder(item)
    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pnj = mDataset[position]

        holder.lineObject.findViewById<ImageView>(R.id.pnj_relation_type)
            .setImageResource(getImageByType(pnj.relationshipType))
        ImageViewCompat.setImageTintList(holder.lineObject.findViewById(R.id.pnj_relation_type),
            ColorStateList.valueOf(getColorByType(pnj.relationshipType)))

        holder.lineObject.findViewById<ConstraintLayout>(R.id.pnj_unfolded).visibility =
            View.GONE
        holder.lineObject.findViewById<ImageView>(R.id.pnj_expand)
            .setImageResource(R.drawable.expand_more)

        holder.lineObject.findViewById<TextView>(R.id.pnj_name).text = pnj.name
        holder.lineObject.findViewById<TextView>(R.id.pnj_species).text = pnj.species
        holder.lineObject.findViewById<TextView>(R.id.pnj_summary).text = pnj.summary
        holder.lineObject.findViewById<TextView>(R.id.pnj_description_value).text = pnj.description

        holder.lineObject.findViewById<ImageView>(R.id.pnj_expand).setOnClickListener {
            if(holder.lineObject.findViewById<ConstraintLayout>(R.id.pnj_unfolded).visibility == View.GONE) {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.pnj_unfolded).visibility = View.VISIBLE
                holder.lineObject.findViewById<ImageView>(R.id.pnj_expand).setImageResource(R.drawable.expand_less)
            } else {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.pnj_unfolded).visibility = View.GONE
                holder.lineObject.findViewById<ImageView>(R.id.pnj_expand).setImageResource(R.drawable.expand_more)
            }
        }
        holder.lineObject.findViewById<ImageView>(R.id.pnj_delete).setOnClickListener {
            callback.onItemClicked(position, holder.lineObject.findViewById(R.id.pnj_delete), R.id.pnj_delete)
        }
        holder.lineObject.findViewById<ImageView>(R.id.pnj_edit).setOnClickListener {
            callback.onItemClicked(position, holder.lineObject.findViewById(R.id.pnj_edit), R.id.pnj_edit)
        }
    }

    private fun getImageByType(relashionshipType: RelationshipTypeEnum): Int {
        return when (relashionshipType) {
            RelationshipTypeEnum.AMICAL -> R.drawable.friendship
            RelationshipTypeEnum.FAMILY -> R.drawable.family
            RelationshipTypeEnum.LOVER ->  R.drawable.love
            RelationshipTypeEnum.NEUTRAL -> R.drawable.ic_baseline_circle_24
            RelationshipTypeEnum.OPPONENT -> R.drawable.opponent
            RelationshipTypeEnum.RIVAL -> R.drawable.rival
        }
    }

    private fun getColorByType(relashionshipType: RelationshipTypeEnum): Int {
        return when (relashionshipType) {
            RelationshipTypeEnum.AMICAL -> ContextCompat.getColor(context, R.color.friend)
            RelationshipTypeEnum.FAMILY -> ContextCompat.getColor(context, R.color.family)
            RelationshipTypeEnum.LOVER ->  ContextCompat.getColor(context, R.color.love)
            RelationshipTypeEnum.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            RelationshipTypeEnum.OPPONENT -> ContextCompat.getColor(context, R.color.opponent)
            RelationshipTypeEnum.RIVAL -> ContextCompat.getColor(context, R.color.rival)
        }
    }

    class ViewHolder(val lineObject: ConstraintLayout) : RecyclerView.ViewHolder(lineObject)
}