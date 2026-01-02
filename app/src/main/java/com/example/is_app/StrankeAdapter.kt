package com.example.is_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StrankeAdapter(private val strankeList: List<Stranka>) :
    RecyclerView.Adapter<StrankeAdapter.StrankaViewHolder>() {

    class StrankaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIme: TextView = itemView.findViewById(R.id.tvIme)
        val tvPriimek: TextView = itemView.findViewById(R.id.tvPriimek)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvTelefon: TextView = itemView.findViewById(R.id.tvTelefon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrankaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stranka, parent, false)
        return StrankaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StrankaViewHolder, position: Int) {
        val currentItem = strankeList[position]
        holder.tvIme.text = currentItem.ime
        holder.tvPriimek.text = currentItem.priimek
        holder.tvEmail.text = currentItem.email
        holder.tvTelefon.text = currentItem.telefon
    }

    override fun getItemCount() = strankeList.size
}