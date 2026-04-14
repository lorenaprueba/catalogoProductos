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
import com.example.catalogoproductos.databinding.FragmentCarritoBinding
import com.example.catalogoproductos.models.Product

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartProducts = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
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
            onQuantityChanged = { updateTotals() },
            onProductRemoved = { updateTotals() }
        )

        binding.cartRecyclerView.adapter = cartAdapter
    }

    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            if (cartProducts.isEmpty()) {
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
        val totalAmount = cartProducts.sumOf { it.getTotalPrice() }
        val itemCount = cartProducts.sumOf { it.quantity }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm_purchase)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<android.widget.TextView>(R.id.dialogItemCount).text =
            "Artículos: $itemCount"
        dialog.findViewById<android.widget.TextView>(R.id.dialogTotalAmount).text =
            "Total: $ ${String.format("%,.0f", totalAmount)}"

        dialog.findViewById<View>(R.id.btnConfirm).setOnClickListener {
            dialog.dismiss()

            cartProducts.clear()
            cartAdapter.notifyDataSetChanged()
            updateTotals()

            showPurchaseSuccessDialog(totalAmount)
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

    private fun addSampleProducts() {
        cartProducts.add(Product(1, "Coco Chanel", 300000.0, 1))
        cartProducts.add(Product(2, "Dior Home", 350000.0, 2))
        cartProducts.add(Product(3, "Boss", 245000.0, 1))

        cartAdapter.notifyDataSetChanged()
        updateTotals()
    }

    private fun updateTotals() {
        val itemCount = cartProducts.sumOf { it.quantity }
        val totalPrice = cartProducts.sumOf { it.getTotalPrice() }

        binding.totalItems.text = "Total de artículos: $itemCount"
        binding.totalPrice.text = "Total: $ ${String.format("%,.0f", totalPrice)}"

        if (cartProducts.isEmpty()) {
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
