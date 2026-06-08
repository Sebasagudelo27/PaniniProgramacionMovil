package com.example.panini.wc2026.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.panini.wc2026.data.entity.Lamina
import com.example.panini.wc2026.databinding.FragmentLaminasBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RepetidasFragment : Fragment() {

    private var _binding: FragmentLaminasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: LaminaAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentLaminasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = LaminaAdapter(TipoLista.REPETIDAS) { laminaEntregada ->
            mostrarDialogoIntercambio(laminaEntregada)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.tvTitulo.text = "Láminas Repetidas"

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.laminasRepetidas.collectLatest { lista ->
                adapter.submitList(lista)
                binding.tvVacia.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                binding.tvVacia.text = if (lista.isEmpty()) "No tienes láminas repetidas" else ""
            }
        }
    }

    private fun mostrarDialogoIntercambio(laminaEntregada: Lamina) {
        viewLifecycleOwner.lifecycleScope.launch {
            val pendientes = viewModel.laminasPendientes.first()

            if (pendientes.isEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Sin pendientes")
                    .setMessage("No tienes láminas pendientes.")
                    .setPositiveButton("OK", null)
                    .show()
                return@launch
            }

            val input = AutoCompleteTextView(requireContext())
            input.hint = "Escribe nombre o número"
            input.setPadding(48, 32, 48, 32)

            val nombres = pendientes.map { "${it.numero} - ${it.nombre}" }
            val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nombres)
            input.setAdapter(arrayAdapter)
            input.threshold = 1

            var seleccionada: Lamina? = null
            input.setOnItemClickListener { _, _, position, _ ->
                val textoSeleccionado = arrayAdapter.getItem(position) ?: return@setOnItemClickListener
                seleccionada = pendientes.find { "${it.numero} - ${it.nombre}" == textoSeleccionado }
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Intercambiar: ${laminaEntregada.nombre}")
                .setMessage("Entregás: ${laminaEntregada.nombre}")
                .setView(input)
                .setPositiveButton("Confirmar") { _, _ ->
                    seleccionada?.let { laminaRecibida ->
                        viewModel.registrarIntercambio(laminaEntregada, laminaRecibida)
                        mostrarConfirmacion(laminaEntregada, laminaRecibida)
                        viewLifecycleOwner.lifecycleScope.launch {
                            val lista = viewModel.laminasRepetidas.first()
                            adapter.submitList(null)
                            adapter.submitList(lista)
                        }
                    } ?: Toast.makeText(requireContext(), "Selecciona una lámina", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun mostrarConfirmacion(entregada: Lamina, recibida: Lamina) {
        AlertDialog.Builder(requireContext())
            .setTitle("✅ Intercambio registrado")
            .setMessage("Entregaste: ${entregada.nombre}\nRecibiste: ${recibida.nombre}")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}