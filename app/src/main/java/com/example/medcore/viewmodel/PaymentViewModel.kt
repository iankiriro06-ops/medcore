package com.example.medcore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medcore.network.MpesaClient
import com.example.medcore.network.MpesaResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class PromptSent(val message: String) : PaymentState()
    data class Success(val message: String) : PaymentState()
    data class Error(val message: String) : PaymentState()
}

class PaymentViewModel : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState

    fun initiatePurchase(phone: String, amount: Int) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            when (val result = MpesaClient.initiateStkPush(phone, amount)) {
                is MpesaResult.Success -> {
                    _paymentState.value = PaymentState.PromptSent(result.message)
                }
                is MpesaResult.Error -> {
                    _paymentState.value = PaymentState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _paymentState.value = PaymentState.Idle
    }
}