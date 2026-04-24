package com.example.catalogoproductos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.databinding.ItemHistorialOrdenBinding
import com.example.catalogoproductos.models.Order
import com.example.catalogoproductos.models.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialAdapter(
    private val ordenes: MutableList<Order>,
    private val itemsProvider: (Long) -> List<OrderItem>,
    private val onReagregarClick: (Order) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.OrdenViewHolder>() {

    private val fechaFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    inner class OrdenViewHolder(private val binding: ItemHistorialOrdenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.txtOrdenFecha.text =
                "Orden del ${fechaFormatter.format(Date(order.fecha))}"
            binding.txtOrdenTotal.text =
                "$ ${String.format("%,.0f", order.total)}"
            binding.txtOrdenItemsCount.text =
                "${order.itemsCount} artículo${if (order.itemsCount == 1) "" else "s"}"

            binding.layoutOrdenItems.removeAllViews()
            val items = itemsProvider(order.id)
            val inflater = LayoutInflater.from(binding.root.context)
            for (item in items) {
                val row = inflater.inflate(
                    R.layout.item_historial_linea,
                    binding.layoutOrdenItems,
                    false
                )
                row.findViewById<TextView>(R.id.txtLineaNombre).text =
                    "• ${item.cantidad}x ${item.nombre} (${item.marca})"
                row.findViewById<TextView>(R.id.txtLineaTotal).text =
                    "$ ${String.format("%,.0f", item.total())}"
                binding.layoutOrdenItems.addView(row)
            }

            binding.btnReagregar.setOnClickListener {
                onReagregarClick(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
        val binding = ItemHistorialOrdenBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OrdenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
        holder.bind(ordenes[position])
    }

    override fun getItemCount(): Int = ordenes.size
}
