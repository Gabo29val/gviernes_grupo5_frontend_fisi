package com.example.dsm_frontend.ui.carModule.carrito

import android.content.Context
import android.util.Log
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.Payment
import com.example.dsm_frontend.data.model.Car
import com.example.dsm_frontend.data.model.ItemCar
import com.example.dsm_frontend.ui.carModule.carrito.adapter.ProductCarAdapter
import com.example.dsm_frontend.databinding.FragmentMainCarBinding
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Specification
import com.example.dsm_frontend.presentation.MainCarViewModel
import com.stripe.android.paymentsheet.PaymentSheetResultCallback

class MainCarFragment : Fragment(R.layout.fragment_main_car) {

    private lateinit var mBinding: FragmentMainCarBinding
    private lateinit var mProductAdapter: ProductCarAdapter
    private val mMainCarVM: MainCarViewModel by viewModels()


    val TAG = "CheckoutActivity"
    val BACKEND_URL = "http://192.168.0.106:8080/stripe"

    //val BACKEND_URL = "http://127.0.0.1:8080/stripe"
    // val BACKEND_URL = "http://10.0.2.2:8080/stripe"
    var paymentIntentClientSecret: String? = null

    lateinit var paymentSheet: PaymentSheet


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainCarBinding.bind(view)

        mProductAdapter = ProductCarAdapter(mMainCarVM)

        mBinding.rvProducts.apply {
            adapter = mProductAdapter
            setHasFixedSize(true)
        }

        mMainCarVM.amountTotalLD.observe(viewLifecycleOwner, {
            mBinding.tvAmountToPay.text = it.toString()
        })

        PaymentConfiguration.init(
            requireContext(),
            "pk_test_51KM3QZG6zQNEntYYBTviK5vbbI0sloSqUoJ5ZFcpcjkApfD83KE53soTFIBYqZDWNqfRnGJvOvQmw3AmVRVojCPq00XmTq9dLN"
        );

        mBinding.btnPay.setOnClickListener {
            pay()
        }

        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentSheetResult(
                paymentSheetResult!!

            )
        }

        /*paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentSheetResult(
                paymentSheetResult!!
            )
        }*/

        //fetchPaymentIntent()
    }

    private fun pay() {

        if (mMainCarVM.amountTotalLD.value!! >= 1.0) {
            mBinding.progressBar.visibility = View.VISIBLE
            fetchPaymentIntent()
        }

    }

    fun showAlert(title: String, message: String?) {
        activity?.let {
            it.runOnUiThread {
                val dialog =
                    AlertDialog.Builder(mBinding.root.context)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Ok", null)
                        .create()
                dialog.show()
                //dialog.dismiss()
                println(message)
                mBinding.progressBar.visibility = View.GONE
            }
        }
    }

    fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun fetchPaymentIntent() {
        //val shoppingCartContent = "{\"id\":  ${binding.editText.text.toString()}}"
        //val shoppingCartContent = "{\"totalAmount\":  ${mBinding.tvAmountToPay.text.toString()}}"

        val shoppingCartContent =
            "{\"totalAmount\":  ${(mMainCarVM.amountTotalLD.value!! * 100).toInt()}}"

        //Toast.makeText(mBinding.root.context, shoppingCartContent, Toast.LENGTH_SHORT).show()

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
                    println("El error onFailure  es $e")
                    showAlert("Failed to load data", "Error: $e")
                    //showToast("Falló al cargar onFailure")
                }

                @Throws(IOException::class)
                override fun onResponse(
                    call: Call,
                    response: Response
                ) {

                    if (!response.isSuccessful) {
                        println("El error onResponse es $response")

                        showAlert(
                            "Failed to load page",
                            "Error: $response"
                        )
                        //showToast("Falló al cargar")
                        mBinding.progressBar.visibility = View.GONE

                    } else {
                        val responseJson = parseResponse(response.body)
                        paymentIntentClientSecret = responseJson.optString("clientSecret")
                        activity!!.runOnUiThread {

                            onPayClicked()
                            mBinding.progressBar.visibility = View.GONE
                            //mBinding.btnPay!!.isEnabled = true

                        }

                        Log.i(TAG, "Retrieved PaymentIntent")
                        // onPayClicked()
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

    fun onPayClicked() {

        val configuration = PaymentSheet.Configuration("Example, Inc.")

        // Present Payment Sheet
        paymentSheet?.let {
            paymentIntentClientSecret?.let { it1 ->
                it.presentWithPaymentIntent(
                    it1,
                    configuration
                )
            }
        }
        //paymentSheet!!.presentWithPaymentIntent(paymentIntentClientSecret!!, configuration)
    }

    fun onPaymentSheetResult(
        paymentSheetResult: PaymentSheetResult
    ) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            showToast("Payment complete!")
            Car.items.clear()
            (mBinding.rvProducts.adapter as ProductCarAdapter).notifyDataSetChanged()
            mMainCarVM.updateTotalAmount()

        } else if (paymentSheetResult is PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!")
        } else if (paymentSheetResult is PaymentSheetResult.Failed) {
            val error: Throwable = (paymentSheetResult as PaymentSheetResult.Failed).error
            showAlert("Payment failed", error.localizedMessage)
        }
    }


}