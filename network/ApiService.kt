package com.nolgaemi.servermonitorapp.network

import com.nolgaemi.servermonitorapp.model.ServerStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/agent/{id}")
    fun getServerStatus(@Path("id") id: Int): Call<ServerStatus>
}
