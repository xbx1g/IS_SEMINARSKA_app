package com.example.is_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MehanikiAdapter(private val mehanikiList: List<Mehanik>) :
    RecyclerView.Adapter<MehanikiAdapter.MehanikViewHolder>() {

    class MehanikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIme: TextView = itemView.findViewById(R.id.tvIme)
        val tvPriimek: TextView = itemView.findViewById(R.id.tvPriimek)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvTelefon: TextView = itemView.findViewById(R.id.tvTelefon)
        val tvSpecializacija: TextView = itemView.findViewById(R.id.tvSpecializacija)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MehanikViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mehanik, parent, false)
        return MehanikViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MehanikViewHolder, position: Int) {
        val currentItem = mehanikiList[position]
        holder.tvIme.text = currentItem.ime
        holder.tvPriimek.text = currentItem.priimek
        holder.tvEmail.text = currentItem.email
        holder.tvTelefon.text = currentItem.telefon
        holder.tvSpecializacija.text = currentItem.specializacija
    }

    override fun getItemCount() = mehanikiList.size
}