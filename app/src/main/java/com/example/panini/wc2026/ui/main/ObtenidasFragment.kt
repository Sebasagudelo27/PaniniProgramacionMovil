package com.example.panini.wc2026.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panini.wc2026.data.entity.Lamina
import com.example.panini.wc2026.databinding.FragmentLaminasBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ObtenidasFragment : Fragment() {

    private var _binding: FragmentLaminasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var listaCompleta: List<Lamina> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentLaminasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LaminaAdapter(TipoLista.OBTENIDAS) { lamina ->
            viewModel.registrarLaminaObtenida(lamina)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.tvTitulo.text = "Láminas Obtenidas"

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtro = newText?.lowercase() ?: ""
                val filtrada = listaCompleta.filter {
                    it.nombre.lowercase().contains(filtro) || it.pais.lowercase().contains(filtro)
                }
                adapter.submitList(filtrada)
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.laminasObtenidas.collectLatest { lista ->
                listaCompleta = lista
                adapter.submitList(lista)
                binding.tvVacia.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                binding.tvVacia.text = if (lista.isEmpty()) "Aún no tienes láminas obtenidas" else ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}