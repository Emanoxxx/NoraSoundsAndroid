package com.emanoxxxpc.nora.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoraApi {
    companion object {
        fun getApiSession(): NoraApiService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NoraApiService::class.java)
        }
    }
}