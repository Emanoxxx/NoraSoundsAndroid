package com.emanoxxxpc.nora.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoraApi {
    companion object {
        fun getApiSession(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://Mac-mini-de-Ale.local/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}