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
import com.example.catalogoproductos.data.CartRepository
import com.example.catalogoproductos.databinding.FragmentCarritoBinding
import com.example.catalogoproductos.models.CartItem

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private lateinit var repository: CartRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = CartRepository(requireContext())

        setupRecyclerView()
        setupCheckoutButton()
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems,
            onQuantityChanged = { item ->
                repository.actualizarCantidad(item.id, item.cantidad)
                updateTotals()
            },
            onProductRemoved = { item ->
                repository.eliminarItem(item.id)
                updateTotals()
            }
        )
        binding.cartRecyclerView.adapter = cartAdapter
    }

    private fun cargarCarrito() {
        cartItems.clear()
        cartItems.addAll(repository.obtenerCarrito())
        cartAdapter.notifyDataSetChanged()
        updateTotals()
    }

    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            if (cartItems.isEmpty()) {
                showEmptyCartDialog()
            } else {
                showConfirmPurchaseDialog()
            }
        }
    }

    private fun showEmptyCartDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_empty_cart)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<View>(R.id.btnAccept).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showConfirmPurchaseDialog() {
        val totalAmount = cartItems.sumOf { it.total() }
        val itemCount = cartItems.sumOf { it.cantidad }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm_purchase)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val puntosAGanar = ((totalAmount / 1000) * 4).toInt()

        dialog.findViewById<android.widget.TextView>(R.id.dialogItemCount).text =
            "Artículos: $itemCount"
        dialog.findViewById<android.widget.TextView>(R.id.dialogTotalAmount).text =
            "Total: $ ${String.format("%,.0f", totalAmount)}"
        dialog.findViewById<android.widget.TextView>(R.id.dialogPuntosAGanar).text =
            "Puntos a ganar: $puntosAGanar"

        dialog.findViewById<View>(R.id.btnConfirm).setOnClickListener {
            dialog.dismiss()

            val orderId = repository.confirmarCompra()
            cartItems.clear()
            cartAdapter.notifyDataSetChanged()
            updateTotals()

            if (orderId > 0) {
                showPurchaseSuccessDialog(totalAmount)
            }
        }

        dialog.findViewById<View>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showPurchaseSuccessDialog(totalAmount: Double) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_purchase_success)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<android.widget.TextView>(R.id.dialogSuccessTotal).text =
            "Total: $ ${String.format("%,.0f", totalAmount)}"

        dialog.findViewById<View>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateTotals() {
        val itemCount = cartItems.sumOf { it.cantidad }
        val totalPrice = cartItems.sumOf { it.total() }

        binding.totalItems.text = "Total de artículos: $itemCount"
        binding.totalPrice.text = "Total: $ ${String.format("%,.0f", totalPrice)}"

        if (cartItems.isEmpty()) {
            binding.emptyCartText.visibility = View.VISIBLE
            binding.cartRecyclerView.visibility = View.GONE
        } else {
            binding.emptyCartText.visibility = View.GONE
            binding.cartRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
