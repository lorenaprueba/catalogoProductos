package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.FavoritosAdapter
import com.example.catalogoproductos.data.FavoritoDao

class FavoritosFragment : Fragment() {

    private lateinit var dao: FavoritoDao
    private lateinit var adapter: FavoritosAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var tvVacio: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_favoritos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = FavoritoDao(requireContext())

        recycler = view.findViewById(R.id.recycler_favoritos)
        tvVacio = view.findViewById(R.id.tv_sin_favoritos)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargarFavoritos()
    }

    // Se ejecuta cada vez que vuelves al fragment
    override fun onResume() {
        super.onResume()
        if (::dao.isInitialized) {
            cargarFavoritos()
        }
    }

    private fun cargarFavoritos() {
        try {
            val lista = dao.obtenerTodos().toMutableList()

            adapter = FavoritosAdapter(
                lista = lista,
                onEliminar = { producto, position ->
                    dao.eliminar(producto.id.toString())
                    adapter.eliminarEn(position)

                    // Validar si quedó vacío después de eliminar
                    mostrarVacio(adapter.itemCount == 0)
                }
            )

            recycler.adapter = adapter

            // VALIDACIÓN PRINCIPAL
            mostrarVacio(lista.isEmpty())

        } catch (e: Exception) {
            // ERROR CONTROLADO
            tvVacio.text = "Error al cargar favoritos"
            tvVacio.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        }
    }

    private fun mostrarVacio(vacio: Boolean) {
        tvVacio.visibility = if (vacio) View.VISIBLE else View.GONE
        recycler.visibility = if (vacio) View.GONE else View.VISIBLE
    }
}