package com.samprama.mobileapp.webservice

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("waInstance/{idInstance}/sendMessage/{apiTokenInstance}")
    suspend fun sendMessage(
        @retrofit2.http.Path("idInstance") idInstance: String,
        @retrofit2.http.Path("apiTokenInstance") apiTokenInstance: String,
        @Body request: SendMessageRequest
    ): SendMessageResponse
}

data class SendMessageRequest(
    val chatId: String,
    val message: String
)

data class SendMessageResponse(
    val success: Boolean
)
