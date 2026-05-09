package com.example.medcore.network


import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// ── M-Pesa Sandbox Credentials ───────────────────────────────────────────────
// WARNING: These are sandbox credentials only — safe for testing, no real money
private const val CONSUMER_KEY    = "8Gfatc9TrbPP05zby7BjdRkKoGayXmgAzDYG0lNXYKAReKOU"
private const val CONSUMER_SECRET = "8KJi2bFd0RLL0dMEgS1OCwr09Jck995LlbcaZcB8qVz57KgTtT6mPbq092miAeCd"
private const val SHORTCODE       = "174379"
private const val PASSKEY         = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
private const val BASE_URL        = "https://sandbox.safaricom.co.ke"

// Use a public echo server as callback for sandbox testing
private const val CALLBACK_URL    = "https://webhook.site/your-unique-id"

// ── Result sealed class ──────────────────────────────────────────────────────

sealed class MpesaResult {
    data class Success(val checkoutRequestId: String, val message: String) : MpesaResult()
    data class Error(val message: String) : MpesaResult()
}

// ── MpesaClient ──────────────────────────────────────────────────────────────

object MpesaClient {

    // Step 1 — Get access token from Safaricom
    private suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        val credentials = Base64.encodeToString(
            "$CONSUMER_KEY:$CONSUMER_SECRET".toByteArray(),
            Base64.NO_WRAP
        )
        val url = URL("$BASE_URL/oauth/v1/generate?grant_type=client_credentials")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Authorization", "Basic $credentials")
        }
        val response = conn.inputStream.bufferedReader().readText()
        conn.disconnect()
        Log.d("MpesaClient", "Token response: $response")
        JSONObject(response).getString("access_token")
    }

    // Step 2 — Format phone number to 254XXXXXXXXX
    private fun formatPhone(raw: String): String = when {
        raw.startsWith("0")  -> "254" + raw.substring(1)
        raw.startsWith("+")  -> raw.substring(1)
        raw.startsWith("254") -> raw
        else                 -> raw
    }

    // Step 3 — Initiate STK Push
    suspend fun initiateStkPush(
        phone: String,
        amount: Int
    ): MpesaResult = withContext(Dispatchers.IO) {
        try {
            val token     = getAccessToken()
            val timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            val password  = Base64.encodeToString(
                "$SHORTCODE$PASSKEY$timestamp".toByteArray(),
                Base64.NO_WRAP
            )
            val formattedPhone = formatPhone(phone)

            val payload = JSONObject().apply {
                put("BusinessShortCode", SHORTCODE)
                put("Password",          password)
                put("Timestamp",         timestamp)
                put("TransactionType",   "CustomerPayBillOnline")
                put("Amount",            amount)
                put("PartyA",            formattedPhone)
                put("PartyB",            SHORTCODE)
                put("PhoneNumber",       formattedPhone)
                put("CallBackURL",       CALLBACK_URL)
                put("AccountReference",  "MedCore")
                put("TransactionDesc",   "MedCore Subscription")
            }

            val url  = URL("$BASE_URL/mpesa/stkpush/v1/processrequest")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
            }

            OutputStreamWriter(conn.outputStream).use { it.write(payload.toString()) }

            val responseCode = conn.responseCode
            val response = if (responseCode == 200) {
                conn.inputStream.bufferedReader().readText()
            } else {
                conn.errorStream.bufferedReader().readText()
            }
            conn.disconnect()

            Log.d("MpesaClient", "STK response: $response")
            val json = JSONObject(response)

            return@withContext if (json.optString("ResponseCode") == "0") {
                MpesaResult.Success(
                    checkoutRequestId = json.getString("CheckoutRequestID"),
                    message           = json.optString("CustomerMessage", "Check your phone for the M-Pesa prompt")
                )
            } else {
                MpesaResult.Error(
                    json.optString("errorMessage", "Payment failed. Please try again.")
                )
            }

        } catch (e: Exception) {
            Log.e("MpesaClient", "STK push error", e)
            MpesaResult.Error("Connection error: ${e.message}")
        }
    }
}