package com.example.catalogoproductos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.models.Producto
import com.google.android.material.button.MaterialButton

class FavoritosAdapter(
    private val lista: MutableList<Producto>,
    private val onEliminar: (Producto, Int) -> Unit
) : RecyclerView.Adapter<FavoritosAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorito, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount() = lista.size

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView       = itemView.findViewById(R.id.tv_nombre_fav)
        private val btnElim:  MaterialButton = itemView.findViewById(R.id.btn_eliminar_fav)

        fun bind(producto: Producto) {
            tvNombre.text = producto.nombre
            btnElim.setOnClickListener {
                val currentPos = adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    onEliminar(producto, currentPos)
                }
            }
        }
    }

    fun eliminarEn(position: Int) {
        lista.removeAt(position)
        notifyItemRemoved(position)
    }
}