package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.HistorialAdapter
import com.example.catalogoproductos.data.CartRepository
import com.example.catalogoproductos.models.Order

class ProfileFragment : Fragment() {

    private lateinit var repository: CartRepository
    private lateinit var historialAdapter: HistorialAdapter
    private val ordenes = mutableListOf<Order>()

    private lateinit var layoutHistorial: LinearLayout
    private lateinit var rvHistorial: RecyclerView
    private lateinit var txtHistorialVacio: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = CartRepository(requireContext())

        val btnHistorial = view.findViewById<Button>(R.id.btnHistorial)
        val btnFavoritos = view.findViewById<Button>(R.id.btnFavoritos)
        layoutHistorial = view.findViewById(R.id.layoutHistorial)
        rvHistorial = view.findViewById(R.id.rvHistorial)
        txtHistorialVacio = view.findViewById(R.id.txtHistorialVacio)

        setupHistorialRecyclerView()

        btnHistorial.setOnClickListener {
            if (layoutHistorial.visibility == View.GONE) {
                cargarHistorial()
                layoutHistorial.visibility = View.VISIBLE
            } else {
                layoutHistorial.visibility = View.GONE
            }
        }

        btnFavoritos.setOnClickListener {
            view.findNavController().navigate(R.id.favoritosFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::layoutHistorial.isInitialized &&
            layoutHistorial.visibility == View.VISIBLE
        ) {
            cargarHistorial()
        }
    }

    private fun setupHistorialRecyclerView() {
        historialAdapter = HistorialAdapter(
            ordenes = ordenes,
            itemsProvider = { orderId -> repository.obtenerItemsDeOrden(orderId) },
            onReagregarClick = { order ->
                val cantidad = repository.reagregarOrdenAlCarrito(order.id)
                Toast.makeText(
                    requireContext(),
                    "$cantidad producto${if (cantidad == 1) "" else "s"} agregado${if (cantidad == 1) "" else "s"} al carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        rvHistorial.layoutManager = LinearLayoutManager(requireContext())
        rvHistorial.adapter = historialAdapter
    }

    private fun cargarHistorial() {
        ordenes.clear()
        ordenes.addAll(repository.obtenerOrdenes())
        historialAdapter.notifyDataSetChanged()

        if (ordenes.isEmpty()) {
            txtHistorialVacio.visibility = View.VISIBLE
            rvHistorial.visibility = View.GONE
        } else {
            txtHistorialVacio.visibility = View.GONE
            rvHistorial.visibility = View.VISIBLE
        }
    }
}
