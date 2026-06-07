package com.example.panini.wc2026.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.panini.wc2026.data.entity.Lamina
import com.example.panini.wc2026.databinding.ItemLaminaBinding

enum class TipoLista { PENDIENTES, OBTENIDAS, REPETIDAS }

class LaminaAdapter(
    private val tipo: TipoLista,
    private val onAction: (Lamina) -> Unit
) : ListAdapter<Lamina, LaminaAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemLaminaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemLaminaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val lamina = getItem(position)
        with(holder.binding) {
            tvNumero.text = lamina.numero
            tvNombre.text = lamina.nombre
            tvPais.text = lamina.pais

            when (tipo) {
                TipoLista.PENDIENTES -> {
                    tvEstado.text = "PENDIENTE"
                    tvEstado.setBackgroundResource(android.R.color.holo_red_light)
                    tvRepetidas.visibility = android.view.View.GONE
                    btnAccion.text = "Obtener"
                    btnAccion.setOnClickListener { onAction(lamina) }
                }
                TipoLista.OBTENIDAS -> {
                    tvEstado.text = "OBTENIDA ✓"
                    tvEstado.setBackgroundResource(android.R.color.holo_green_light)
                    tvRepetidas.visibility = android.view.View.GONE
                    btnAccion.text = "Registrar repetida"
                    btnAccion.setOnClickListener { onAction(lamina) }
                }
                TipoLista.REPETIDAS -> {
                    tvEstado.text = "REPETIDA"
                    tvEstado.setBackgroundResource(android.R.color.holo_orange_light)
                    tvRepetidas.visibility = android.view.View.VISIBLE
                    tvRepetidas.text = "Cantidad: ${lamina.cantidadRepetidas}"
                    btnAccion.text = "Intercambiar"
                    btnAccion.setOnClickListener { onAction(lamina) }
                }
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Lamina>() {
            override fun areItemsTheSame(a: Lamina, b: Lamina) = a.id == b.id
            override fun areContentsTheSame(a: Lamina, b: Lamina) = a == b
        }
    }
}
