package com.rp.historiacompagnon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.entity.Item
import com.rp.historiacompagnon.entity.Stuff
import com.rp.historiacompagnon.enum.ItemTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewItemClickListener

class ItemAdapter(
    var mDataset: MutableList<Item>,
    callback: RecyclerViewItemClickListener
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var callback = callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_item, parent, false) as ConstraintLayout

        return ViewHolder(item)
    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stuff = mDataset[position]

        holder.lineObject.findViewById<ImageView>(R.id.item_type)
            .setImageResource(getImageByType(stuff))

        holder.lineObject.findViewById<ConstraintLayout>(R.id.item_unfolded).visibility =
            View.GONE
        holder.lineObject.findViewById<ImageView>(R.id.item_expand)
            .setImageResource(R.drawable.expand_more)

        holder.lineObject.findViewById<TextView>(R.id.item_name).text = stuff.name
        holder.lineObject.findViewById<TextView>(R.id.item_description).text = stuff.description
        holder.lineObject.findViewById<TextView>(R.id.item_quantity).text = stuff.quantity.toString()

        holder.lineObject.findViewById<ImageView>(R.id.item_expand).setOnClickListener {
            if(holder.lineObject.findViewById<ConstraintLayout>(R.id.item_unfolded).visibility == View.GONE) {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.item_unfolded).visibility = View.VISIBLE
                holder.lineObject.findViewById<ImageView>(R.id.item_expand).setImageResource(R.drawable.expand_less)
            } else {
                holder.lineObject.findViewById<ConstraintLayout>(R.id.item_unfolded).visibility = View.GONE
                holder.lineObject.findViewById<ImageView>(R.id.item_expand).setImageResource(R.drawable.expand_more)
            }
        }
        holder.lineObject.findViewById<ImageView>(R.id.item_delete).setOnClickListener {
            callback.onStuffItemClicked(stuff, position, R.id.item_delete)
        }
        holder.lineObject.findViewById<ImageView>(R.id.item_edit).setOnClickListener {
            callback.onStuffItemClicked(stuff, position, R.id.item_edit)
        }
        holder.lineObject.findViewById<ImageView>(R.id.item_minus_one).setOnClickListener {
            callback.onStuffItemClicked(stuff, position, R.id.item_minus_one)
        }
        holder.lineObject.findViewById<ImageView>(R.id.item_plus_one).setOnClickListener {
            callback.onStuffItemClicked(stuff, position, R.id.item_plus_one)
        }
    }

    private fun getImageByType(stuff: Stuff): Int {
        return when ((stuff as Item).type) {
                ItemTypeEnum.QUEST -> R.drawable.quest
                ItemTypeEnum.CONSUMABLE -> R.drawable.consumable
                ItemTypeEnum.POTION -> R.drawable.potion
                ItemTypeEnum.SCROLL -> R.drawable.scroll
                ItemTypeEnum.TOOL -> R.drawable.tool
                else -> R.drawable.item
            }
    }

    class ViewHolder(val lineObject: ConstraintLayout) : RecyclerView.ViewHolder(lineObject)

}