package com.example.panini.wc2026.ui.buscador

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panini.wc2026.databinding.ActivityBuscadorBinding
import com.example.panini.wc2026.ui.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BuscadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscadorBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "🔍 Buscar Jugador"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = JugadorAdapter()
        binding.recyclerJugadores.layoutManager = LinearLayoutManager(this)
        binding.recyclerJugadores.adapter = adapter

        binding.btnBuscar.setOnClickListener {
            val query = binding.etBusqueda.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.buscarJugadorEnApi(query)
            }
        }

        // Observar estado de carga
        lifecycleScope.launch {
            viewModel.buscandoJugador.collectLatest { cargando ->
                binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
                binding.btnBuscar.isEnabled = !cargando
            }
        }

        // Observar resultados
        lifecycleScope.launch {
            viewModel.busquedaJugador.collectLatest { jugadores ->
                jugadores?.let {
                    adapter.submitList(it)
                    binding.tvSinResultados.visibility = View.GONE
                }
            }
        }

        // Observar errores
        lifecycleScope.launch {
            viewModel.errorBusqueda.collectLatest { error ->
                error?.let {
                    binding.tvSinResultados.text = it
                    binding.tvSinResultados.visibility = View.VISIBLE
                    adapter.submitList(emptyList())
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.limpiarBusqueda()
    }
}
