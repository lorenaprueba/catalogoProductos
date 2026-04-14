package com.example.catalogoproductos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.catalogoproductos.databinding.FragmentWebBinding

class WebFragment : Fragment() {

    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    private val defaultUrl = "https://www.dior.com/es_sam"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        configureWebView()
        setupUrlBar()
        
        // Cargar página inicial de Dior
        loadUrl(defaultUrl)
    }

    private fun setupUrlBar() {
        // Al presionar Enter en el teclado
        binding.etUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrlFromEditText()
                true
            } else false
        }

        // Al presionar el botón de flecha al lado de la barra
        binding.btnGo.setOnClickListener {
            loadUrlFromEditText()
        }
    }

    private fun loadUrlFromEditText() {
        var url = binding.etUrl.text.toString().trim()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            loadUrl(url)
        }
    }

    private fun loadUrl(url: String) {
        binding.webView.loadUrl(url)
        binding.etUrl.setText(url)
    }

    private fun configureWebView() {
        with(binding.webView) {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    _binding?.let { 
                        it.webProgressBar.isVisible = newProgress in 1..99
                        it.webProgressBar.progress = newProgress
                    }
                }
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    _binding?.let {
                        it.webProgressBar.isVisible = false
                        it.loadingCenter.isVisible = false
                        it.etUrl.setText(url) // Actualizar barra con la URL final
                    }
                }
            }

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = true
                displayZoomControls = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.webView.stopLoading()
        _binding = null
        super.onDestroyView()
    }
}
