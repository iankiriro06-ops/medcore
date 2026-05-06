package com.example.medcore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medcore.network.PaymentRequest
import com.example.medcore.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class PromptSent(val message: String, val checkoutRequestId: String) : PaymentState()
    data class Success(val message: String) : PaymentState()
    data class Error(val message: String) : PaymentState()
}

class PaymentViewModel : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState

    fun initiatePurchase(phone: String, amount: Int) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                val response = RetrofitClient.paymentApi.initiateStkPush(
                    PaymentRequest(phone, amount)
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.ResponseCode == "0") {
                        val checkoutId = body.CheckoutRequestID
                        _paymentState.value = PaymentState.PromptSent(
                            body.CustomerMessage, checkoutId
                        )
                        pollPaymentStatus(checkoutId)
                    } else {
                        _paymentState.value = PaymentState.Error("Payment request failed")
                    }
                } else {
                    _paymentState.value = PaymentState.Error("Server error")
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun pollPaymentStatus(checkoutRequestId: String) {
        viewModelScope.launch {
            repeat(10) {
                delay(5000)
                try {
                    val response = RetrofitClient.paymentApi.checkPaymentStatus(checkoutRequestId)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.paid == true) {
                            _paymentState.value = PaymentState.Success("Payment confirmed!")
                            return@launch
                        }
                    }
                } catch (e: Exception) {
                    println("Poll error: ${e.message}")
                }
            }
        }
    }

    fun resetState() {
        _paymentState.value = PaymentState.Idle
    }
}