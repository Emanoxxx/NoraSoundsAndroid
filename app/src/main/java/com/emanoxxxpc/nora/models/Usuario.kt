package com.emanoxxxpc.nora.models

import com.google.gson.annotations.SerializedName

data class Usuario(
    val username: String,
    val password: String,
    val nombre: String? = null,
    val email: String? = null,
    val isActive: Boolean? = null,
    val isAdmin: Boolean? = null,
    val token: String? = null,
    val id: String? = null,
)
