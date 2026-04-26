package com.example.catalogoproductos.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val sharedPref = requireContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "usuario@gmail.com")

        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreo)
        val txtStatPedidos = view.findViewById<TextView>(R.id.txtStatPedidos)
        val txtStatFavoritos = view.findViewById<TextView>(R.id.txtStatFavoritos)
        val txtStatPuntos = view.findViewById<TextView>(R.id.txtStatPuntos)

        val btnRowHistorial = view.findViewById<LinearLayout>(R.id.btnRowHistorial)
        val btnRowCerrarSesion = view.findViewById<LinearLayout>(R.id.btnRowCerrarSesion)
        
        val txtMisFavoritosLabel = view.findViewById<TextView>(R.id.txtMisFavoritosLabel)
        val layoutFavoritosContainer = view.findViewById<LinearLayout>(R.id.layoutFavoritosContainer)

        layoutHistorial = view.findViewById(R.id.layoutHistorial)
        rvHistorial = view.findViewById(R.id.rvHistorial)
        txtHistorialVacio = view.findViewById(R.id.txtHistorialVacio)

        txtNombre.text = "Lucia Hernandez" // Dummy name
        txtCorreo.text = userEmail

        setupHistorialRecyclerView()

        btnRowHistorial.setOnClickListener {
            if (layoutHistorial.visibility == View.GONE) {
                cargarHistorial()
                layoutHistorial.visibility = View.VISIBLE
            } else {
                layoutHistorial.visibility = View.GONE
            }
        }


        txtMisFavoritosLabel.setOnClickListener {
            view.findNavController().navigate(R.id.favoritosFragment)
        }

        layoutFavoritosContainer.setOnClickListener {
            view.findNavController().navigate(R.id.favoritosFragment)
        }

        btnRowCerrarSesion.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        cargarStats(txtStatPedidos, txtStatPuntos)
        // Favoritos dinámicos
        cargarFavoritosUI(view)
    }

    override fun onResume() {
        super.onResume()
        if (::layoutHistorial.isInitialized &&
            layoutHistorial.visibility == View.VISIBLE
        ) {
            cargarHistorial()
        }
        val view = this.view
        if (view != null) {
            val txtStatPedidos = view.findViewById<TextView>(R.id.txtStatPedidos)
            val txtStatPuntos = view.findViewById<TextView>(R.id.txtStatPuntos)
            cargarStats(txtStatPedidos, txtStatPuntos)
            cargarFavoritosUI(view)
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

    private fun cargarStats(txtStatPedidos: TextView, txtStatPuntos: TextView) {
        val totalOrdenes = repository.obtenerOrdenes()
        txtStatPedidos.text = totalOrdenes.size.toString()
        
        var puntosTotales = 0
        for (orden in totalOrdenes) {
            puntosTotales += ((orden.total / 1000) * 4).toInt()
        }
        
        txtStatPuntos.text = String.format("%,d", puntosTotales).replace(',', '.')
    }

    private fun cargarFavoritosUI(view: View) {
        val favoritos = repository.obtenerFavoritos()
        val txtStatFavoritos = view.findViewById<TextView>(R.id.txtStatFavoritos)
        txtStatFavoritos.text = favoritos.size.toString()

        val containers = listOf(
            view.findViewById<LinearLayout>(R.id.containerFav1),
            view.findViewById<LinearLayout>(R.id.containerFav2),
            view.findViewById<LinearLayout>(R.id.containerFav3)
        )
        val images = listOf(
            view.findViewById<android.widget.ImageView>(R.id.imgFav1),
            view.findViewById<android.widget.ImageView>(R.id.imgFav2),
            view.findViewById<android.widget.ImageView>(R.id.imgFav3)
        )
        val texts = listOf(
            view.findViewById<TextView>(R.id.txtFav1),
            view.findViewById<TextView>(R.id.txtFav2),
            view.findViewById<TextView>(R.id.txtFav3)
        )

        for (i in 0..2) {
            if (i < favoritos.size) {
                containers[i].visibility = View.VISIBLE
                images[i].setImageResource(favoritos[i].imagen)
                texts[i].text = favoritos[i].nombre
            } else {
                containers[i].visibility = View.INVISIBLE
            }
        }
    }
}
