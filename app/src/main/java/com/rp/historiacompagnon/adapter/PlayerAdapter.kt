package com.rp.historiacompagnon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class PlayerAdapter (var mDataset: MutableList<Character>, callback : RecyclerViewClickListener, characterKey: String) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    private var callback = callback
    var characterKey = characterKey

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_player, parent, false) as LinearLayout

        return ViewHolder(item)
    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = mDataset[position]
        if(character.key == characterKey) {
            holder.lineObject.findViewById<MaterialCardView>(R.id.player_card).isChecked = true
        }
        holder.lineObject.findViewById<TextView>(R.id.player_name).text = character.concatFullName()
        holder.lineObject.findViewById<TextView>(R.id.player_familia).text = character.race.familia
        holder.lineObject.findViewById<TextView>(R.id.player_age).text = character.age.toString()
        holder.lineObject.findViewById<TextView>(R.id.player_job).text = character.jobs.joinToString(separator = "-") { it -> it.name }

        holder.lineObject.findViewById<MaterialCardView>(R.id.player_card).setOnClickListener {
            callback.onItemClicked(position, holder.lineObject.findViewById(R.id.player_card), R.id.player_card)
        }
    }

    class ViewHolder(val lineObject: LinearLayout) : RecyclerView.ViewHolder(lineObject)
}