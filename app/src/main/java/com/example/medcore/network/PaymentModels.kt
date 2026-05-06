package com.example.medcore.network



data class PaymentRequest(
    val phone: String,
    val amount: Int
)

data class PaymentResponse(
    val CheckoutRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String,
    val CustomerMessage: String
)

data class PaymentStatusResponse(
    val paid: Boolean,
    val result_desc: String
)