package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.ProductoAdapter
import com.example.catalogoproductos.data.CartRepository
import com.example.catalogoproductos.models.Producto

class FavoritosFragment : Fragment() {

    private lateinit var repository: CartRepository
    private lateinit var adapter: ProductoAdapter
    private val listaFavoritos = mutableListOf<Producto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = CartRepository(requireContext())
        
        val rvFavoritos = view.findViewById<RecyclerView>(R.id.rvFavoritos)
        val txtNoFavoritos = view.findViewById<TextView>(R.id.txtNoFavoritos)

        adapter = ProductoAdapter(listaFavoritos)
        adapter.onFavoriteClick = { producto, isFavorite ->
            if (isFavorite) {
                repository.agregarFavorito(producto)
            } else {
                repository.eliminarFavorito(producto)
                // Remove instantly from view for better UX
                val index = listaFavoritos.indexOfFirst { it.nombre == producto.nombre && it.marca == producto.marca }
                if (index != -1) {
                    listaFavoritos.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    
                    if (listaFavoritos.isEmpty()) {
                        txtNoFavoritos.visibility = View.VISIBLE
                        rvFavoritos.visibility = View.GONE
                    }
                }
            }
        }

        rvFavoritos.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFavoritos.adapter = adapter

        cargarFavoritos(rvFavoritos, txtNoFavoritos)
    }

    private fun cargarFavoritos(rvFavoritos: RecyclerView, txtNoFavoritos: TextView) {
        listaFavoritos.clear()
        listaFavoritos.addAll(repository.obtenerFavoritos())
        adapter.notifyDataSetChanged()

        if (listaFavoritos.isEmpty()) {
            txtNoFavoritos.visibility = View.VISIBLE
            rvFavoritos.visibility = View.GONE
        } else {
            txtNoFavoritos.visibility = View.GONE
            rvFavoritos.visibility = View.VISIBLE
        }
    }
}
