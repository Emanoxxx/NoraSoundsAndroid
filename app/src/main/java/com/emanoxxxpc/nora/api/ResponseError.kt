package com.emanoxxxpc.nora.api

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody

class ResponseError {
    lateinit var error: String

    companion object {
        fun parseResponseErrorBody(errorBody: ResponseBody): ResponseError {
            val gson = Gson()
            val parser = JsonParser()

            val jsonString = errorBody.string()
            val mJson = parser.parse(jsonString)
            val error = gson.fromJson(mJson, ResponseError::class.java)
            return error;
        }
    }
}
