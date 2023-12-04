package net.softglobe.paymentgatewaytutorial

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("payment-fetch.php")
    suspend fun fetchPaymentDetails() : Response<PaymentDetailsResponse>
}