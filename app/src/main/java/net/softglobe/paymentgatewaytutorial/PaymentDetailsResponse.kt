package net.softglobe.paymentgatewaytutorial

data class PaymentDetailsResponse(
    val customer: String,
    val ephemeralKey: String,
    val paymentIntent: String,
    val publishableKey: String
)