package com.example.panini.wc2026.ui.buscador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.panini.wc2026.R
import com.example.panini.wc2026.data.db.Player
import com.example.panini.wc2026.databinding.ItemJugadorBinding

class JugadorAdapter : ListAdapter<Player, JugadorAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemJugadorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemJugadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val jugador = getItem(position)
        with(holder.binding) {
            tvNombreJugador.text = jugador.nombre ?: "Sin nombre"
            tvNacionalidad.text = "🌍 ${jugador.nacionalidad ?: "Desconocida"}"
            tvEquipo.text = "🏟️ ${jugador.equipo ?: "Sin equipo"}"
            tvPosicion.text = "👟 ${jugador.posicion ?: "Sin posición"}"
            tvFechaNac.text = "🎂 ${jugador.fechaNacimiento ?: "N/A"}"
            tvAltura.text = "📏 ${jugador.altura ?: "N/A"}"

            val desc = jugador.descripcion
            if (!desc.isNullOrBlank()) {
                tvDescripcion.text = desc.take(200) + if (desc.length > 200) "..." else ""
                tvDescripcion.visibility = android.view.View.VISIBLE
            } else {
                tvDescripcion.visibility = android.view.View.GONE
            }

            if (!jugador.fotoUrl.isNullOrBlank()) {
                Glide.with(ivFoto.context)
                    .load(jugador.fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .centerCrop()
                    .into(ivFoto)
            } else {
                ivFoto.setImageResource(R.drawable.ic_person)
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(a: Player, b: Player) = a.nombre == b.nombre
            override fun areContentsTheSame(a: Player, b: Player) = a == b
        }
    }
}
