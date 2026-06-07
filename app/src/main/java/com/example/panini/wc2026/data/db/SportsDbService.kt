package com.example.panini.wc2026.data.db

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsDbApi {
    @GET("api/v1/json/3/searchplayers.php")
    suspend fun buscarJugador(@Query("p") nombre: String): PlayerSearchResponse
}

object SportsDbService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.thesportsdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SportsDbApi = retrofit.create(SportsDbApi::class.java)
}
