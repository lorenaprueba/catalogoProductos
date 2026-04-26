package com.example.catalogoproductos.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.models.Producto
import com.example.catalogoproductos.ui.DetalleFragment

class InicioProductoAdapter(private val lista: List<Producto>) :
    RecyclerView.Adapter<InicioProductoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imgInicioProducto)
        val nombre: TextView = view.findViewById(R.id.txtInicioNombre)
        val precio: TextView = view.findViewById(R.id.txtInicioPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inicio_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = lista[position]
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        holder.imagen.setImageResource(producto.imagen)

        // Evento de clic para abrir el detalle
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("nombre", producto.nombre)
                putString("marca", producto.marca)
                putDouble("precio", producto.precio)
                putString("descripcion", producto.descripcion)
                putInt("imagen", producto.imagen)
            }

            holder.itemView.findNavController().navigate(R.id.detalleFragment, bundle)
        }
    }

    override fun getItemCount() = lista.size
}