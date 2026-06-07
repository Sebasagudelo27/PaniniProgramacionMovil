package com.example.panini.wc2026.data.dao

import androidx.room.*
import com.example.panini.wc2026.data.entity.Lamina
import kotlinx.coroutines.flow.Flow

@Dao
interface LaminaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarLamina(lamina: Lamina)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(laminas: List<Lamina>)

    @Update
    suspend fun actualizarLamina(lamina: Lamina)

    @Query("SELECT * FROM laminas WHERE obtenida = 0 ORDER BY id ASC")
    fun getLaminasPendientes(): Flow<List<Lamina>>

    @Query("SELECT * FROM laminas WHERE obtenida = 1 ORDER BY id ASC")
    fun getLaminasObtenidas(): Flow<List<Lamina>>

    @Query("SELECT * FROM laminas WHERE cantidadRepetidas > 0 ORDER BY id ASC")
    fun getLaminasRepetidas(): Flow<List<Lamina>>

    @Query("SELECT * FROM laminas WHERE id = :id")
    suspend fun getLaminaPorId(id: Int): Lamina?

    @Query("SELECT COUNT(*) FROM laminas")
    suspend fun getTotalLaminas(): Int

    @Query("SELECT COUNT(*) FROM laminas WHERE obtenida = 1")
    fun getTotalObtenidas(): Flow<Int>

    @Query("SELECT COUNT(*) FROM laminas WHERE obtenida = 0")
    fun getTotalPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM laminas WHERE cantidadRepetidas > 0")
    fun getTotalRepetidas(): Flow<Int>

    @Query("SELECT * FROM laminas ORDER BY id ASC")
    fun getTodas(): Flow<List<Lamina>>

    @Query("SELECT * FROM laminas WHERE nombre LIKE '%' || :query || '%' OR pais LIKE '%' || :query || '%'")
    fun buscarLaminas(query: String): Flow<List<Lamina>>
}
