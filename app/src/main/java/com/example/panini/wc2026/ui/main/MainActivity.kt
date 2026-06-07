package com.example.panini.wc2026.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.panini.wc2026.databinding.ActivityMainBinding
import com.example.panini.wc2026.ui.buscador.BuscadorActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "⚽ Álbum Mundial 2026"

        // ViewPager con los 3 tabs
        val fragments = listOf(
            PendientesFragment(),
            ObtenidasFragment(),
            RepetidasFragment()
        )
        binding.viewPager.adapter = AlbumPagerAdapter(this, fragments)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> "PENDIENTES"
                1 -> "OBTENIDAS"
                else -> "REPETIDAS"
            }
        }.attach()

        // Contadores en tiempo real
        lifecycleScope.launch {
            viewModel.totalObtenidas.collectLatest { total ->
                binding.tvObtenidas.text = "Obtenidas: $total"
            }
        }
        lifecycleScope.launch {
            viewModel.totalPendientes.collectLatest { total ->
                binding.tvPendientes.text = "Pendientes: $total"
            }
        }
        lifecycleScope.launch {
            viewModel.totalRepetidas.collectLatest { total ->
                binding.tvRepetidas.text = "Repetidas: $total"
            }
        }

        // Botón buscador API
        binding.fabBuscar.setOnClickListener {
            startActivity(Intent(this, BuscadorActivity::class.java))
        }
    }
}
