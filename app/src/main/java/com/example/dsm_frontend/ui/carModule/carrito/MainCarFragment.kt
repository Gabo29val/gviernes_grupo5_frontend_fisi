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
import com.example.dsm_frontend.R
import com.example.dsm_frontend.api.Payment
import com.example.dsm_frontend.ui.carModule.carrito.adapter.ProductCarAdapter
import com.example.dsm_frontend.databinding.FragmentMainCarBinding
import com.example.dsm_frontend.data.model.Product
import com.example.dsm_frontend.data.model.Specification
import com.stripe.android.paymentsheet.PaymentSheetResultCallback

class MainCarFragment : Fragment(R.layout.fragment_main_car) {

    private lateinit var mBinding: FragmentMainCarBinding
    private lateinit var mProductAdapter: ProductCarAdapter
    val TAG = "CheckoutActivity"
    val BACKEND_URL = "http://192.168.0.106:8080/stripe"
    //val BACKEND_URL = "http://127.0.0.1:8080/stripe"
   // val BACKEND_URL = "http://10.0.2.2:8080/stripe"
    var paymentIntentClientSecret: String? = null
    lateinit var paymentSheet: PaymentSheet
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentMainCarBinding.bind(view)

        mProductAdapter = ProductCarAdapter(getProducts())

        mBinding.rvProducts.apply {
            adapter = mProductAdapter
            setHasFixedSize(true)
        }
        //val payment : Payment =Payment(mBinding.tvAmountToPay.text.toString(),mBinding.btnPay, requireContext())
        /*mBinding.btnPay.setOnClickListener{
            println("Pagando ...")
            requireActivity().runOnUiThread {
                payment!!.onPayClicked(view)
            }
        }*/
        PaymentConfiguration.init(
            requireContext(),
            "pk_test_51KM3QZG6zQNEntYYBTviK5vbbI0sloSqUoJ5ZFcpcjkApfD83KE53soTFIBYqZDWNqfRnGJvOvQmw3AmVRVojCPq00XmTq9dLN"
        );/*
        //mBinding.btnPay.setEnabled(false)
        requireActivity().runOnUiThread {
            payment!!.paymentSheet = PaymentSheet(
                this
            ) { paymentSheetResult: PaymentSheetResult ->
                payment!!.onPaymentSheetResult(
                    paymentSheetResult
                )
            }
        }
        requireActivity().runOnUiThread {    payment!!.fetchPaymentIntent()}*/

        // Hook up the pay button

        mBinding.btnPay.setOnClickListener{

            onPayClicked()
            //initPasarela()
        }



        mBinding.btnPay.setEnabled(false)

        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentSheetResult(
                paymentSheetResult!!
            )
        }

        fetchPaymentIntent()
    }
    private fun initPasarela(){
        paymentSheet = PaymentSheet(
            this
        ) { paymentSheetResult: PaymentSheetResult? ->
            onPaymentSheetResult(
                paymentSheetResult!!

            )
        }

        fetchPaymentIntent()
        //onPayClicked()

    }
    private fun getProducts(): List<Product> {
        return listOf(
            Product(
                name = "Red label 1",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                stock = 10,
                specifications = mutableListOf(
                    Specification("Presentación", "Botella"),
                    Specification("Composición", "Grano y malta"),
                    Specification("Proceso de añejamiento", "No declarada"),
                    Specification("Volumen neto", "750ml"),
                )
            ),
            Product(
                name = "Red label 2",
                photoUrl = "https://www.blogdelfotografo.com/wp-content/uploads/2020/12/producto_fondo_negro.webp",
                nameStore = "Tottus",
                price = 99.99,
                description = "Dentro de los licores encontramos al Whisky, una bebida alcohólica a base de malta fermentada de cereales como cebada, trigo, centeno y maíz, que se destila y añeja en barriles de madera tradicionalmente de roble blanco. Este último proceso dura por lo menos tres años para que adquiera el color caramelo que lo caracteriza. El Wisky tiene sus orígenes en Irlanda y Escocia y en la actualidad se disfruta en muchos países a nivel mundial.",
                stock = 10,
                specifications = mutableListOf(
                    Specification("Presentación", "Botella"),
                    Specification("Composición", "Grano y malta"),
                    Specification("Proceso de añejamiento", "No declarada"),
                    Specification("Volumen neto", "750ml"),
                )
            ),
        )
    }
    fun showAlert(title: String, message: String?) {

        requireActivity().runOnUiThread  {
            val dialog =
                AlertDialog.Builder(mBinding.root.context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create()
            dialog.show()
            //dialog.dismiss()
            println(message)
        }

    }

    fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun fetchPaymentIntent() {
        //val shoppingCartContent = "{\"id\":  ${binding.editText.text.toString()}}"
        val shoppingCartContent = "{\"totalAmount\":  ${mBinding.tvAmountToPay.text.toString()}}"

        val requestBody: RequestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),shoppingCartContent
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

                    } else {
                        val responseJson = parseResponse(response.body)
                        paymentIntentClientSecret = responseJson.optString("clientSecret")
                         activity!!.runOnUiThread {
                             mBinding.btnPay!!.isEnabled = true

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