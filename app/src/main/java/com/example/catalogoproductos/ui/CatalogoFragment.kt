package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.ProductoAdapter
import com.example.catalogoproductos.models.Producto

class CatalogoFragment : Fragment(R.layout.fragment_catalogo) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter

    private lateinit var listaOriginal: List<Producto>
    private var listaFiltrada: MutableList<Producto> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.rvProductos)

        val edtBuscar = view.findViewById<EditText>(R.id.edtBuscar)
        val btnTodos = view.findViewById<Button>(R.id.btnTodos)
        val btnMasc = view.findViewById<Button>(R.id.btnMasc)
        val btnFem = view.findViewById<Button>(R.id.btnFem)

        // Lista de productos
        listaOriginal = listOf(
            Producto("Coco Chanel", "Chanel", 300000.0, R.drawable.chanel, "Sumérgete en la sofisticación pura con este icónico perfume de Chanel, una fragancia que encarna la esencia del lujo y la feminidad. Su delicada mezcla de notas \n", listOf("femenino")),
            Producto("Boss", "Hugo Boss", 245000.0, R.drawable.boss, "Una fragancia intensa y elegante que combina frescura y carácter. Perfecta para quienes proyectan seguridad, estilo y una presencia inolvidable en cualquier ocasión", listOf("masculino")),
            Producto("Dior Home", "Dior", 350000.0, R.drawable.dior, "Una fragancia sofisticada y envolvente que combina notas amaderadas con un toque moderno. Ideal para hombres seguros, con estilo y personalidad refinada.", listOf("masculino")),
            Producto("Guess for women", "Guess", 250000.0, R.drawable.guess, "Una fragancia juvenil y femenina que combina notas frescas y dulces, perfecta para resaltar tu encanto natural con un toque moderno y sofisticado.", listOf("femenino"))
        )

        listaFiltrada = listaOriginal.toMutableList()

        adapter = ProductoAdapter(listaFiltrada)

        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = adapter

        // BUSCAR
        edtBuscar.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                filtrar(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // FILTROS
        btnTodos.setOnClickListener {
            listaFiltrada.clear()
            listaFiltrada.addAll(listaOriginal)
            adapter.notifyDataSetChanged()
        }

        btnMasc.setOnClickListener {
            filtrarPorCategoria("masculino")
        }

        btnFem.setOnClickListener {
            filtrarPorCategoria("femenino")
        }
    }

    // Filtrar por texto
    private fun filtrar(texto: String) {
        listaFiltrada.clear()

        for (producto in listaOriginal) {
            if (producto.nombre.contains(texto, true) ||
                producto.marca.contains(texto, true)
            ) {
                listaFiltrada.add(producto)
            }
        }

        adapter.notifyDataSetChanged()
    }

    // Filtrar por categoría
    private fun filtrarPorCategoria(categoria: String) {
        listaFiltrada.clear()

        for (producto in listaOriginal) {
            if (producto.notas.contains(categoria)) {
                listaFiltrada.add(producto)
            }
        }

        adapter.notifyDataSetChanged()
    }
}
