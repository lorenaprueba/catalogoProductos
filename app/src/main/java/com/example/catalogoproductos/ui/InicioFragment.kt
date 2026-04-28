package com.example.catalogoproductos.ui

/**
 * Fragmento de Inicio optimizado: Gestión de video local con portada y navegación mejorada.
 */
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogoproductos.R
import com.example.catalogoproductos.adapters.InicioProductoAdapter
import com.example.catalogoproductos.databinding.FragmentInicioBinding
import com.example.catalogoproductos.models.Producto

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            configurarRecyclerView()
            configureVideoPlayer()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error desconocido",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnExplorarCatalogo.setOnClickListener {
            findNavController().navigate(R.id.catalogoFragment)
        }

        binding.imgVideoCover.visibility = View.VISIBLE
        binding.viewVideoOverlay.visibility = View.VISIBLE
        binding.videoPerfume.visibility = View.GONE
    }

    private fun configurarRecyclerView() {
        val listaProductos = mutableListOf(
            Producto("Coco Chanel", "Chanel", 300000.0, R.drawable.chanel, "Sumérgete en la sofisticación pura con este icónico perfume de Chanel.", listOf("femenino")),
            Producto("Boss", "Hugo Boss", 245000.0, R.drawable.boss, "Una fragancia intensa y elegante que combina frescura y carácter.", listOf("masculino")),
            Producto("Dior Home", "Dior", 350000.0, R.drawable.dior, "Una fragancia sofisticada y envolvente amaderada.", listOf("masculino")),
            Producto("Guess for women", "Guess", 250000.0, R.drawable.guess, "Una fragancia juvenil y femenina que combina notas frescas.", listOf("femenino"))
        )

        val adapter = InicioProductoAdapter(listaProductos)
        binding.rvInicioProductos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvInicioProductos.adapter = adapter
    }

    private fun configureVideoPlayer() {
        val videoPath = "android.resource://${requireContext().packageName}/${R.raw.video_perfume}"
        binding.videoPerfume.setVideoURI(Uri.parse(videoPath))

        // El botón central externo controla el Play/Pause
        binding.btnPlayPauseExternal.setOnClickListener {
            if (binding.videoPerfume.isPlaying) {
                pauseVideo()
            } else {
                startVideo()
            }
        }

        // Al tocar la portada también inicia
        binding.imgVideoCover.setOnClickListener { startVideo() }

        // Botones externos de control
        binding.btnRewind.setOnClickListener {
            val currentPos = binding.videoPerfume.currentPosition
            binding.videoPerfume.seekTo(currentPos - 5000)
        }

        binding.btnForward.setOnClickListener {
            val currentPos = binding.videoPerfume.currentPosition
            binding.videoPerfume.seekTo(currentPos + 5000)
        }

        // Tocar el video para pausar
        binding.videoPerfume.setOnClickListener {
            if (binding.videoPerfume.isPlaying) pauseVideo() else startVideo()
        }

        binding.videoPerfume.setOnCompletionListener {
            resetVideoUI()
        }
        
        binding.videoPerfume.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
    }

    private fun startVideo() {
        binding.imgVideoCover.visibility = View.GONE
        binding.viewVideoOverlay.visibility = View.GONE
        binding.videoPerfume.visibility = View.VISIBLE
        binding.videoPerfume.start()
        
        // Cambiar icono a Pausa
        binding.btnPlayPauseExternal.setIconResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseVideo() {
        binding.videoPerfume.pause()
        // Cambiar icono a Play
        binding.btnPlayPauseExternal.setIconResource(android.R.drawable.ic_media_play)
    }

    private fun resetVideoUI() {
        binding.imgVideoCover.visibility = View.VISIBLE
        binding.viewVideoOverlay.visibility = View.VISIBLE
        binding.videoPerfume.visibility = View.GONE
        binding.btnPlayPauseExternal.setIconResource(android.R.drawable.ic_media_play)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
