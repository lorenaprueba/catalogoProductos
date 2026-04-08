package com.example.catalogoproductos.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.CartAdapter
import com.example.catalogoproductos.databinding.FragmentCartBinding
import com.example.catalogoproductos.models.Product

class CartFragment : Fragment() {

    // Binding para acceder a los elementos de la UI
    private lateinit var binding: FragmentCartBinding

    // Adaptador para mostrar la lista de productos
    private lateinit var cartAdapter: CartAdapter

    // Lista de productos parael carrito
    private val cartProducts = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCheckoutButton()
        addSampleProducts()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartProducts,
            onQuantityChanged = { updateTotals() },  // Recalcula totales al cambiar cantidad
            onProductRemoved = { updateTotals() }    // Recalcual totales al eliminar producto
        )

        binding.cartRecyclerView.adapter = cartAdapter
    }

    // Configura el botón de finalizar compra con validaicones
    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            // Valida que el carrito no esté vacío
            if (cartProducts.isEmpty()) {
                showEmptyCartDialog()
            } else {
                showConfirmPurchaseDialog()
            }
        }
    }

    // Muestra diálogo personalizado de advertencia cuando el carrito está vacío
    private fun showEmptyCartDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_empty_cart)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Botón "Entendido" cierra el diálogo
        dialog.findViewById<View>(R.id.btnAccept).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Muestra diálogo personalizado de confirmación con el resumen de la compra
    private fun showConfirmPurchaseDialog() {
        val totalAmount = cartProducts.sumOf { it.getTotalPrice() }
        val itemCount = cartProducts.sumOf { it.quantity }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm_purchase)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Define la cantidad de artículso y el total en el diálogo
        dialog.findViewById<android.widget.TextView>(R.id.dialogItemCount).text =
            "Artículos: $itemCount"
        dialog.findViewById<android.widget.TextView>(R.id.dialogTotalAmount).text =
            "Total: $ ${String.format("%,.0f", totalAmount)}"

        // Botón "Comprar ahora" confirma la compra
        dialog.findViewById<View>(R.id.btnConfirm).setOnClickListener {
            dialog.dismiss()

            // Limpia el carrito después de confirmar
            cartProducts.clear()
            cartAdapter.notifyDataSetChanged()
            updateTotals()

            // Muestra diálogo de compra exitosa
            showPurchaseSuccessDialog(totalAmount)
        }

        // Botón "Cancelar" cierra el diálogo sin hacer nada
        dialog.findViewById<View>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Muestra diálogo personalizado de éxito después de finalizar la compra
    private fun showPurchaseSuccessDialog(totalAmount: Double) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_purchase_success)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Establece el monto total pagado
        dialog.findViewById<android.widget.TextView>(R.id.dialogSuccessTotal).text =
            "Total: $ ${String.format("%,.0f", totalAmount)}"

        // Botón "Aceptar" cierra el diálogo
        dialog.findViewById<View>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Agrega productos de ejemplo al carrito
    private fun addSampleProducts() {
        cartProducts.add(Product(1, "Perfume A - Edición Premium", 180000.0, 1))
        cartProducts.add(Product(2, "Cologne B - Fresh Breeze", 70000.0, 2))
        cartProducts.add(Product(3, "Fragancia C - Elegante", 120000.0, 1))

        cartAdapter.notifyDataSetChanged()

        // Actualiza el total y la información mostrada
        updateTotals()
    }

    // Actualiza los totales mostrados (cantidad de articulos y precoi total)
    private fun updateTotals() {
        val itemCount = cartProducts.sumOf { it.quantity }  // Suma las cantidades
        val totalPrice = cartProducts.sumOf { it.getTotalPrice() }  // Suma el precio total

        // Actualiza el texto de cantidad total de artículos
        binding.totalItems.text = "Total de artículos: $itemCount"

        // Actualiza el texto del precio total
        binding.totalPrice.text = "Total: $ ${String.format("%,.0f", totalPrice)}"

        // Controla la visibilidad del mensaje de carrito vacío
        if (cartProducts.isEmpty()) {
            binding.emptyCartText.visibility = View.VISIBLE      // Muestra carrito vacío
            binding.cartRecyclerView.visibility = View.GONE      // Oculta la lista
        } else {
            binding.emptyCartText.visibility = View.GONE         // Oculta carrito vacío
            binding.cartRecyclerView.visibility = View.VISIBLE   // Muestra la lista
        }
    }
}
