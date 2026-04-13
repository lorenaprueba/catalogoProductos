package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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

        val layoutHistorial =
            view.findViewById<LinearLayout>(R.id.layoutHistorial)

        val btnComprar1 =
            view.findViewById<Button>(R.id.btnComprar1)

        val btnComprar2 =
            view.findViewById<Button>(R.id.btnComprar2)

        val btnComprar3 =
            view.findViewById<Button>(R.id.btnComprar3)

        val btnComprar4 =
            view.findViewById<Button>(R.id.btnComprar4)

        btnHistorial.setOnClickListener {

            if (layoutHistorial.visibility == View.GONE) {

                layoutHistorial.visibility = View.VISIBLE

                Toast.makeText(
                    requireContext(),
                    "Mostrando historial",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                layoutHistorial.visibility = View.GONE

            }

        }

        btnComprar1.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnComprar2.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnComprar3.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Añadido al carrito",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnComprar4.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Añadido al carrito",
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

    }
}
