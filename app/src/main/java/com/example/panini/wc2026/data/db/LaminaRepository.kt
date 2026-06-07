package com.example.panini.wc2026.data.db

import com.example.panini.wc2026.data.entity.Lamina
import kotlinx.coroutines.flow.Flow

class LaminaRepository(private val db: AppDatabase) {

    val laminasPendientes: Flow<List<Lamina>> = db.laminaDao().getLaminasPendientes()
    val laminasObtenidas: Flow<List<Lamina>> = db.laminaDao().getLaminasObtenidas()
    val laminasRepetidas: Flow<List<Lamina>> = db.laminaDao().getLaminasRepetidas()
    val totalObtenidas: Flow<Int> = db.laminaDao().getTotalObtenidas()
    val totalPendientes: Flow<Int> = db.laminaDao().getTotalPendientes()
    val totalRepetidas: Flow<Int> = db.laminaDao().getTotalRepetidas()

    suspend fun registrarObtenida(lamina: Lamina) {
        if (!lamina.obtenida) {
            // Primera vez que la obtenemos
            lamina.obtenida = true
            db.laminaDao().actualizarLamina(lamina)
        } else {
            // Ya la teníamos → es repetida
            lamina.cantidadRepetidas += 1
            db.laminaDao().actualizarLamina(lamina)
        }
    }

    suspend fun registrarIntercambio(laminaEntregada: Lamina, laminaRecibida: Lamina) {
        // Disminuir repetidas de la que entregamos
        if (laminaEntregada.cantidadRepetidas > 0) {
            laminaEntregada.cantidadRepetidas -= 1
            db.laminaDao().actualizarLamina(laminaEntregada)
        }
        // Marcar como obtenida la que recibimos
        if (!laminaRecibida.obtenida) {
            laminaRecibida.obtenida = true
            db.laminaDao().actualizarLamina(laminaRecibida)
        }
    }

    suspend fun getLaminaPorId(id: Int): Lamina? = db.laminaDao().getLaminaPorId(id)

    fun buscarLaminas(query: String): Flow<List<Lamina>> = db.laminaDao().buscarLaminas(query)

    fun getTodas(): Flow<List<Lamina>> = db.laminaDao().getTodas()
}
