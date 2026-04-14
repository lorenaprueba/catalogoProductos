package com.example.catalogoproductos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.catalogoproductos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.navigationRail.setupWithNavController(navController)

        // Forzar navegación manual si es necesario (Opcional pero recomendado para depurar)
        binding.navigationRail.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicioFragment -> {
                    navController.navigate(R.id.inicioFragment)
                    true
                }
                R.id.catalogoFragment -> {
                    navController.navigate(R.id.catalogoFragment)
                    true
                }
                R.id.favoritosFragment -> {
                    navController.navigate(R.id.favoritosFragment)
                    true
                }
                R.id.carritoFragment -> {
                    navController.navigate(R.id.carritoFragment)
                    true
                }
                R.id.perfilFragment -> {
                    navController.navigate(R.id.perfilFragment)
                    true
                }
                R.id.webFragment -> {
                    navController.navigate(R.id.webFragment)
                    true
                }
                else -> false
            }
        }
    }
}