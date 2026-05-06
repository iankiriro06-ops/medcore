package com.example.medcore.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {
    @POST("api/payment/initiate/")
    suspend fun initiateStkPush(
        @Body request: PaymentRequest
    ): Response<PaymentResponse>

    @GET("api/payment/status/{checkout_request_id}/")
    suspend fun checkPaymentStatus(
        @Path("checkout_request_id") checkoutRequestId: String
    ): Response<PaymentStatusResponse>
}