package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.catalogoproductos.R
import androidx.navigation.findNavController

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

        btnHistorial.setOnClickListener {
            Toast.makeText(context, "Historial en desarrollo", Toast.LENGTH_SHORT).show()
        }

        btnFavoritos.setOnClickListener {
            Toast.makeText(context, "Favoritos en desarrollo", Toast.LENGTH_SHORT).show()
        }
    }
}
