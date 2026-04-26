package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.catalogoproductos.data.CartRepository
import com.example.catalogoproductos.models.Producto
import com.example.catalogoproductos.databinding.FragmentDetalleBinding

class DetalleFragment : Fragment() {

    private var _binding: FragmentDetalleBinding? = null
    private val binding get() = _binding!!

    private lateinit var producto: Producto

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        try {

            val nombre = arguments?.getString("nombre") ?: ""
            val marca = arguments?.getString("marca") ?: ""
            val precio = arguments?.getDouble("precio") ?: 0.0
            val descripcion = arguments?.getString("descripcion") ?: ""
            val imagen = arguments?.getInt("imagen") ?: 0

            producto = Producto(
                nombre,
                marca,
                precio,
                imagen,
                descripcion,
                listOf()
            )

            binding.txtNombreDetalle.text = nombre
            binding.txtMarcaDetalle.text = marca
            binding.txtPrecioDetalle.text = "$$precio"
            binding.txtDescripcionDetalle.text = descripcion
            binding.imgDetalle.setImageResource(imagen)

        }
        catch (e: Exception) {

            Toast.makeText(
                requireContext(),
                "Error al cargar detalle del producto",
                Toast.LENGTH_SHORT
            ).show()

        }

        binding.btnAgregarDetalle.setOnClickListener {

            try {

                CartRepository(requireContext())
                    .agregarAlCarrito(producto)

                Toast.makeText(
                    requireContext(),
                    "Agregado al carrito",
                    Toast.LENGTH_SHORT
                ).show()

            }
            catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error al agregar producto al carrito",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null

    }

}
