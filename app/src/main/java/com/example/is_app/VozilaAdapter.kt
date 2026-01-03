package com.example.is_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VozilaAdapter(private val vozilaList: List<Vozilo>) :
    RecyclerView.Adapter<VozilaAdapter.VoziloViewHolder>() {

    class VoziloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvZnamka: TextView = itemView.findViewById(R.id.tvZnamka)
        val tvModel: TextView = itemView.findViewById(R.id.tvModel)
        val tvRegistracija: TextView = itemView.findViewById(R.id.tvRegistracija)
        val tvLetnik: TextView = itemView.findViewById(R.id.tvLetnik)
        val tvVIN: TextView = itemView.findViewById(R.id.tvVIN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoziloViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vozilo, parent, false)
        return VoziloViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VoziloViewHolder, position: Int) {
        val currentItem = vozilaList[position]
        holder.tvZnamka.text = "Znamka: ${currentItem.znamka}"
        holder.tvModel.text = "Model: ${currentItem.model}"
        holder.tvRegistracija.text = "Registracija: ${currentItem.registracija}"
        holder.tvLetnik.text = "Letnik: ${currentItem.letnik}"
    }

    override fun getItemCount() = vozilaList.size
}