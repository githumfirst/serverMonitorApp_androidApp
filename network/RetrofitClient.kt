package com.nolgaemi.servermonitorapp.network

import com.nolgaemi.servermonitorapp.model.ServerStatus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitClient {
    private const val BASE_URL = "http://<serverip>/"

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }
}

interface Api {
    @GET("api/agent/{id}")
    fun getServerStatus(@Path("id") id: Int): Call<ServerStatus>

    @GET("api/servers")
    fun getServers(): Call<List<ServerStatus>>
}
