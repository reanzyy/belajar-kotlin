package com.latihan.belajarstorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FurnitureAdapter(private val furnitureList: List<Furniture>) :
    RecyclerView.Adapter<FurnitureAdapter.FurnitureViewHolder>() {

    class FurnitureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomor: TextView = view.findViewById(R.id.tvNomor)
        val tvNamaItem: TextView = view.findViewById(R.id.tvNamaItem)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return FurnitureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FurnitureViewHolder, position: Int) {
        val item = furnitureList[position]
        holder.tvNomor.text = (position + 1).toString()
        holder.tvNamaItem.text = item.nama

        holder.btnEdit.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit ${item.nama}", Toast.LENGTH_SHORT).show()
        }

        holder.btnDelete.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Hapus ${item.nama}", Toast.LENGTH_SHORT).show()
        }
        
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Klik Item: ${item.nama}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = furnitureList.size
}
