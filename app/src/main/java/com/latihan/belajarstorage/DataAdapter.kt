package com.latihan.belajarstorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter untuk menampilkan data pada RecyclerView dengan tombol Ubah dan Hapus
 */
class DataAdapter(
    private val listData: ArrayList<DataItem>,
    private val onEditClick: (item: DataItem, position: Int) -> Unit,
    private val onDeleteClick: (item: DataItem, position: Int) -> Unit
) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomor: TextView = view.findViewById(R.id.tvNomor)
        val tvNamaItem: TextView = view.findViewById(R.id.tvNamaItem)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.tvNomor.text = (position + 1).toString()
        holder.tvNamaItem.text = item.nama

        holder.btnEdit.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                onEditClick(listData[currentPos], currentPos)
            }
        }

        holder.btnDelete.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                onDeleteClick(listData[currentPos], currentPos)
            }
        }
    }

    override fun getItemCount(): Int = listData.size
}
