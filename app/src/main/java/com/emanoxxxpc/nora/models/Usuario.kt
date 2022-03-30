package com.emanoxxxpc.nora.models

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isAdmin") val isAdmin: Boolean,
    @SerializedName("id") val id: Int,

)
