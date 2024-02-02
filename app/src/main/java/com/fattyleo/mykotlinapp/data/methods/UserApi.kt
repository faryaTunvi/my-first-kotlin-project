package com.fattyleo.mykotlinapp.data.methods

import com.fattyleo.mykotlinapp.data.api.ApiClient
import com.fattyleo.mykotlinapp.data.api.request.LoginRequest
import com.fattyleo.mykotlinapp.data.api.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/authaccount/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}