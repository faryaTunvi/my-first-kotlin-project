package com.fattyleo.mykotlinapp.repository

import com.fattyleo.mykotlinapp.data.api.request.LoginRequest
import com.fattyleo.mykotlinapp.data.api.response.LoginResponse
import com.fattyleo.mykotlinapp.data.methods.UserApi
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
        return  UserApi.getApi()?.loginUser(loginRequest = loginRequest)
    }
}