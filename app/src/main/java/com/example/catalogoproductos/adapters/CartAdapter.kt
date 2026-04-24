package com.example.catalogoproductos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.catalogoproductos.databinding.ItemCartProductBinding
import com.example.catalogoproductos.models.CartItem

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQuantityChanged: (CartItem) -> Unit,
    private val onProductRemoved: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.productName.text = item.nombre
            binding.productPrice.text = "$ ${String.format("%,.0f", item.precio)}"
            binding.quantityText.text = item.cantidad.toString()
            binding.totalPrice.text = "Total: $ ${String.format("%,.0f", item.total())}"

            binding.buttonIncrease.setOnClickListener {
                item.cantidad++
                binding.quantityText.text = item.cantidad.toString()
                binding.totalPrice.text = "Total: $ ${String.format("%,.0f", item.total())}"
                onQuantityChanged(item)
            }

            binding.buttonDecrease.setOnClickListener {
                if (item.cantidad > 1) {
                    item.cantidad--
                    binding.quantityText.text = item.cantidad.toString()
                    binding.totalPrice.text = "Total: $ ${String.format("%,.0f", item.total())}"
                    onQuantityChanged(item)
                }
            }

            binding.buttonRemove.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    MaterialAlertDialogBuilder(binding.root.context)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Estás seguro de que deseas eliminar \"${item.nombre}\" del carrito?")
                        .setPositiveButton("Eliminar") { _, _ ->
                            val removed = items.removeAt(position)
                            notifyItemRemoved(position)
                            onProductRemoved(removed)
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
