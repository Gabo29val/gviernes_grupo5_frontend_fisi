package com.example.dsm_frontend.api

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Payment(var amount: String? = "", var payButton: Button, var context: Context) {
    val TAG = "CheckoutActivity"
    //val BACKEND_URL = "http://10.0.2.2:4242"
    val BACKEND_URL = "http://10.0.2.2:4242"

    var paymentIntentClientSecret: String? = null
    var paymentSheet: PaymentSheet? = null

    fun showAlert(title: String, message: String?) {
        val dialog =
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
        dialog.show()

    }

    fun showToast(message: String) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun fetchPaymentIntent() {
        //val shoppingCartContent = "{\"id\":  ${binding.editText.text.toString()}}"
        val shoppingCartContent = "{\"id\":  ${this.amount}}"

        val requestBody: RequestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(), shoppingCartContent
        )
        val request: Request = Request.Builder()
            .url("$BACKEND_URL/create-payment-intent")
            .post(requestBody)
            .build()
        OkHttpClient()
            .newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    showAlert("Failed to load data", "Error: $e")
                }

                @Throws(IOException::class)
                override fun onResponse(
                    call: Call,
                    response: Response
                ) {
                    if (!response.isSuccessful) {
                        showAlert(
                            "Failed to load page",
                            "Error: $response"
                        )

                    } else {
                        val responseJson = parseResponse(response.body)
                        paymentIntentClientSecret = responseJson.optString("clientSecret")
                        //payButton!!.isEnabled = true
                        Log.i(TAG, "Retrieved PaymentIntent")
                    }
                }
            })
    }

    fun parseResponse(responseBody: ResponseBody?): JSONObject {
        if (responseBody != null) {
            try {
                return JSONObject(responseBody.string())
            } catch (e: IOException) {
                Log.e(TAG, "Error parsing response", e)
            } catch (e: JSONException) {
                Log.e(TAG, "Error parsing response", e)
            }
        }
        return JSONObject()
    }

    fun onPayClicked(view: View) {
        val configuration = PaymentSheet.Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet!!.presentWithPaymentIntent(paymentIntentClientSecret!!, configuration)
    }

    fun onPaymentSheetResult(
        paymentSheetResult: PaymentSheetResult
    ) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            showToast("Payment complete!")
        } else if (paymentSheetResult is PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!")
        } else if (paymentSheetResult is PaymentSheetResult.Failed) {
            val error: Throwable = (paymentSheetResult as PaymentSheetResult.Failed).error
            showAlert("Payment failed", error.localizedMessage)
        }
    }
}