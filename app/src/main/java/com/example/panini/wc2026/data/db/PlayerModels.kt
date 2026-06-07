package com.example.panini.wc2026.data.db

import com.google.gson.annotations.SerializedName

data class PlayerSearchResponse(
    @SerializedName("player") val players: List<Player>?
)

data class Player(
    @SerializedName("strPlayer") val nombre: String?,
    @SerializedName("strNationality") val nacionalidad: String?,
    @SerializedName("strTeam") val equipo: String?,
    @SerializedName("strPosition") val posicion: String?,
    @SerializedName("dateBorn") val fechaNacimiento: String?,
    @SerializedName("strHeight") val altura: String?,
    @SerializedName("strWeight") val peso: String?,
    @SerializedName("strDescriptionEN") val descripcion: String?,
    @SerializedName("strThumb") val fotoUrl: String?,
    @SerializedName("strBanner") val bannerUrl: String?
)
