package com.example.catalogoproductos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.catalogoproductos.databinding.ItemCartProductBinding
import com.example.catalogoproductos.models.Product

// Adaptador para mostrar la lista de productos en el carrito
// Gestiona la visualización y las interacciones de cada producto
class CartAdapter(
    private val products: MutableList<Product>,
    private val onQuantityChanged: (Product) -> Unit,  // Callback cuando cambia la cantidad
    private val onProductRemoved: (Product) -> Unit    // Callback cuando se elimina un producto
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder que gestiona la visualización de cada producto del carrito
    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Vincula los datos del producto con los elementos de la UI
        fun bind(product: Product) {
            // Establece el nombre del producto
            binding.productName.text = product.name

            // Establece el precio unitario del producto
            binding.productPrice.text = "$ ${String.format("%,.0f", product.price)}"

            // Establece la cantidad actual del producto
            binding.quantityText.text = product.quantity.toString()

            // Establece el precio total (cantidad * precio unitario)
            binding.totalPrice.text = "Total: $ ${String.format("%,.0f", product.getTotalPrice())}"

            // Botón para aumentar la cantidad del producto
            binding.buttonIncrease.setOnClickListener {
                product.quantity++                      // Incrementa la cantidad
                binding.quantityText.text = product.quantity.toString()  // Actualiza la UI
                binding.totalPrice.text = "Total: $ ${String.format("%,.0f", product.getTotalPrice())}"
                onQuantityChanged(product)              // Notifica al fragment del cambio
            }

            // Botón para disminuir la cantidad del producto
            binding.buttonDecrease.setOnClickListener {
                if (product.quantity > 1) {
                    product.quantity--                  // Decrementa la cantidad (mínimo 1)
                    binding.quantityText.text = product.quantity.toString()
                    binding.totalPrice.text = "Total: $ ${String.format("%,.0f", product.getTotalPrice())}"
                    onQuantityChanged(product)
                }
            }

            // Botón para eliminar el producto del carrito
            // Muestra un diálogo de confirmación antes de eliminar
            binding.buttonRemove.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    MaterialAlertDialogBuilder(binding.root.context)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Estás seguro de que deseas eliminar \"${product.name}\" del carrito?")
                        .setPositiveButton("Eliminar") { _, _ ->
                            products.removeAt(position)         // Elimina el producto de la lista
                            notifyItemRemoved(position)         // Notifica al adaptador del cambio
                            onProductRemoved(product)           // Notifica al fragment de la eliminación
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }
        }
    }

    // Crea una nueva instancia de ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    // Vincula los datos del producto con el ViewHolder
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(products[position])
    }

    // Retorna la cantidad total de productos en el carrito
    override fun getItemCount(): Int = products.size
}
