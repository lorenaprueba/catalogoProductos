package com.example.catalogoproductos.adapters

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogoproductos.R
import com.example.catalogoproductos.data.CartRepository
import com.example.catalogoproductos.models.Producto
import com.example.catalogoproductos.ui.DetalleFragment
import com.example.catalogoproductos.data.FavoritoDao

class ProductoAdapter(
    private val lista: MutableList<Producto>,
    private val esCarrito: Boolean = false
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val precio: TextView = view.findViewById(R.id.txtPrecio)
        val imagen: ImageView = view.findViewById(R.id.imgProducto)
        val boton: Button = view.findViewById(R.id.btnAgregar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)

        val btnFavorito: ImageView = view.findViewById(R.id.btnFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = lista[position]
        val ctx      = holder.itemView.context

        // ── Datos visuales (igual que antes) ──────────────────────────────
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        holder.imagen.setImageResource(producto.imagen)

        // ── CORAZÓN con SQLite ────────────────────────────────────────────
        val dao = FavoritoDao(ctx)

        // Pintar estado inicial desde la BD
        fun actualizarIcono() {
            if (dao.esFavorito(producto.id.toString())) {
                holder.btnFavorito.setImageResource(R.drawable.ic_favorite)
            } else {
                holder.btnFavorito.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        actualizarIcono()   // ← estado correcto al cargar la lista

        holder.btnFavorito.setOnClickListener {
            dao.toggle(producto)   // agrega o elimina de SQLite
            actualizarIcono()      // refresca el ícono según el nuevo estado
        }

        if (esCarrito) {
            holder.boton.text = "Comprar"
            holder.btnEliminar.visibility = View.VISIBLE
        } else {
            holder.boton.text = "Agregar"
            holder.btnEliminar.visibility = View.GONE
        }

        // BOTÓN PRINCIPAL
        holder.boton.setOnClickListener {

            if (esCarrito) {
                Toast.makeText(
                    holder.itemView.context,
                    "Compra individual 🛒",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                CartRepository(holder.itemView.context).agregarAlCarrito(producto)
                Toast.makeText(
                    holder.itemView.context,
                    "Agregado al carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ELIMINAR
        holder.btnEliminar.setOnClickListener {
            CartRepository(holder.itemView.context).eliminarPorProducto(producto)
            lista.removeAt(position)
            notifyItemRemoved(position)
        }

        // DETALLE
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("nombre", producto.nombre)
                putString("marca", producto.marca)
                putDouble("precio", producto.precio)
                putString("descripcion", producto.descripcion)
                putInt("imagen", producto.imagen)
            }

            val fragment = DetalleFragment()
            fragment.arguments = bundle

            val activity = holder.itemView.context as FragmentActivity

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}