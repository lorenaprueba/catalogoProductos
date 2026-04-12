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
import com.example.catalogoproductos.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnHistorial = view.findViewById<Button>(R.id.btnHistorial)
        val btnFavoritos = view.findViewById<Button>(R.id.btnFavoritos)
        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)

        val txtHistorialTitulo =
            view.findViewById<TextView>(R.id.txtHistorialTitulo)

        val itemA =
            view.findViewById<LinearLayout>(R.id.itemHistorialA)

        val itemB =
            view.findViewById<LinearLayout>(R.id.itemHistorialB)

        val itemC =
            view.findViewById<LinearLayout>(R.id.itemHistorialC)

        val btnRecomprarA =
            view.findViewById<Button>(R.id.btnRecomprarA)

        val btnRecomprarB =
            view.findViewById<Button>(R.id.btnRecomprarB)

        val btnRecomprarC =
            view.findViewById<Button>(R.id.btnRecomprarC)

        btnHistorial.setOnClickListener {

            try {

                txtHistorialTitulo.visibility = View.VISIBLE
                itemA.visibility = View.VISIBLE
                itemB.visibility = View.VISIBLE
                itemC.visibility = View.VISIBLE

                Toast.makeText(
                    requireContext(),
                    "Mostrando historial",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error al cargar historial",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        btnRecomprarA.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Perfume A añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnRecomprarB.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Cologne B añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnRecomprarC.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Fragancia C añadida al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnFavoritos.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Favoritos en desarrollo",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnCerrarSesion.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Sesión cerrada",
                Toast.LENGTH_SHORT
            ).show()

        }

    }

}
