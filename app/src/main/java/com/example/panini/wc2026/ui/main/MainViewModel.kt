package com.example.panini.wc2026.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.panini.wc2026.data.db.AppDatabase
import com.example.panini.wc2026.data.db.LaminaRepository
import com.example.panini.wc2026.data.db.SportsDbService
import com.example.panini.wc2026.data.db.Player
import com.example.panini.wc2026.data.entity.Lamina
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LaminaRepository(AppDatabase.getDatabase(application))

    val laminasPendientes = repo.laminasPendientes
    val laminasObtenidas = repo.laminasObtenidas
    val laminasRepetidas = repo.laminasRepetidas
    val totalObtenidas = repo.totalObtenidas
    val totalPendientes = repo.totalPendientes
    val totalRepetidas = repo.totalRepetidas
    val todasLaminas = repo.getTodas()

    private val _busquedaJugador = MutableStateFlow<List<Player>?>(null)
    val busquedaJugador: StateFlow<List<Player>?> = _busquedaJugador

    private val _buscandoJugador = MutableStateFlow(false)
    val buscandoJugador: StateFlow<Boolean> = _buscandoJugador

    private val _errorBusqueda = MutableStateFlow<String?>(null)
    val errorBusqueda: StateFlow<String?> = _errorBusqueda

    fun registrarLaminaObtenida(lamina: Lamina) {
        viewModelScope.launch {
            repo.registrarObtenida(lamina)
        }
    }

    fun registrarIntercambio(laminaEntregada: Lamina, laminaRecibida: Lamina) {
        viewModelScope.launch {
            repo.registrarIntercambio(laminaEntregada, laminaRecibida)
        }
    }

    fun buscarJugadorEnApi(nombre: String) {
        viewModelScope.launch {
            _buscandoJugador.value = true
            _errorBusqueda.value = null
            try {
                val response = SportsDbService.api.buscarJugador(nombre)
                _busquedaJugador.value = response.players
                if (response.players.isNullOrEmpty()) {
                    _errorBusqueda.value = "No se encontró ningún jugador con ese nombre"
                }
            } catch (e: Exception) {
                _errorBusqueda.value = "Error al conectar con la API: ${e.message}"
                _busquedaJugador.value = null
            } finally {
                _buscandoJugador.value = false
            }
        }
    }

    fun limpiarBusqueda() {
        _busquedaJugador.value = null
        _errorBusqueda.value = null
    }

    fun buscarLaminasLocales(query: String) = repo.buscarLaminas(query)

    suspend fun getLaminaPorId(id: Int): Lamina? = repo.getLaminaPorId(id)
}
