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

class FavoritosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_favoritos,
            container,
            false
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout1 =
            view.findViewById<LinearLayout>(R.id.layoutFavorito1)

        val layout2 =
            view.findViewById<LinearLayout>(R.id.layoutFavorito2)

        val layout3 =
            view.findViewById<LinearLayout>(R.id.layoutFavorito3)

        val layout4 =
            view.findViewById<LinearLayout>(R.id.layoutFavorito4)

        val btn1 =
            view.findViewById<Button>(R.id.btnEliminar1)

        val btn2 =
            view.findViewById<Button>(R.id.btnEliminar2)

        val btn3 =
            view.findViewById<Button>(R.id.btnEliminar3)

        val btn4 =
            view.findViewById<Button>(R.id.btnEliminar4)

        btn1.setOnClickListener {

            layout1.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Coco Chanel eliminado",
                Toast.LENGTH_SHORT
            ).show()

        }

        btn2.setOnClickListener {

            layout2.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Boss eliminado",
                Toast.LENGTH_SHORT
            ).show()

        }

        btn3.setOnClickListener {

            layout3.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Dior Homme eliminado",
                Toast.LENGTH_SHORT
            ).show()

        }

        btn4.setOnClickListener {

            layout4.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Guess for Women eliminado",
                Toast.LENGTH_SHORT
            ).show()

        }

    }
}
