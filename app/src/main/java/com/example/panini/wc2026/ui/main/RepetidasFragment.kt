package com.example.panini.wc2026.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentLaminasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LaminaAdapter(TipoLista.REPETIDAS) { laminaEntregada ->
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
                    .setMessage("No tienes láminas pendientes para recibir en un intercambio.")
                    .setPositiveButton("OK", null)
                    .show()
                return@launch
            }

            val nombres = pendientes.map { "${it.numero} - ${it.nombre} (${it.pais})" }.toTypedArray()
            var seleccionada: Lamina? = null
            var indiceSeleccionado = -1

            AlertDialog.Builder(requireContext())
                .setTitle("Intercambiar: ${laminaEntregada.nombre}")
                .setMessage("Entregás: ${laminaEntregada.nombre} (${laminaEntregada.cantidadRepetidas} repetidas)\n\nSeleccioná la lámina que recibís:")
                .setSingleChoiceItems(nombres, -1) { _, which ->
                    indiceSeleccionado = which
                    seleccionada = pendientes[which]
                }
                .setPositiveButton("Confirmar intercambio") { _, _ ->
                    seleccionada?.let { laminaRecibida ->
                        viewModel.registrarIntercambio(laminaEntregada, laminaRecibida)
                        mostrarConfirmacion(laminaEntregada, laminaRecibida)
                    }
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
