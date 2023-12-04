package net.softglobe.paymentgatewaytutorial

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchPaymentDetails()

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        findViewById<Button>(R.id.payment_btn).setOnClickListener {
            presentPaymentSheet()
        }
    }

    private fun fetchPaymentDetails() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.fetchPaymentDetails()
                if (response.isSuccessful && response.body() != null) {
                    val customerId = response.body()!!.customer
                    val ephemeralKey = response.body()!!.ephemeralKey
                    val paymentIntent = response.body()!!.paymentIntent
                    val publishableKeyFromServer = response.body()!!.publishableKey

                    paymentIntentClientSecret = paymentIntent
                    customerConfig = PaymentSheet.CustomerConfiguration( customerId, ephemeralKey)
                    val publishableKey = publishableKeyFromServer
                    PaymentConfiguration.init(this@MainActivity, publishableKey)

                } else {
                    Toast.makeText(this@MainActivity, "Something wrong from server side", Toast.LENGTH_SHORT).show()
                }
            } catch (e : Exception) {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            } finally {
                Toast.makeText(this@MainActivity, "Fetch Payment Details completed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        // implemented in the next steps
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                Toast.makeText(this@MainActivity, "Payment Cancelled", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                Toast.makeText(this@MainActivity, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                Toast.makeText(this@MainActivity, "Payment Successful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Technopoints",
                customer = customerConfig,
            )
        )
    }
}