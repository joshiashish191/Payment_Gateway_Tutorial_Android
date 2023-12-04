<?php
require 'vendor/autoload.php';
$stripe = new \Stripe\StripeClient('PUT_YOUR_SECRET_KEY_HERE');

// Use an existing Customer ID if this is a returning customer.
$customer = $stripe->customers->create([
    'name' => 'Ashish',
    'email' => 'test@example.com'
]);
$ephemeralKey = $stripe->ephemeralKeys->create([
  'customer' => $customer->id,
], [
  'stripe_version' => '2022-08-01',
]);
$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1099,
  'currency' => 'inr',
  'customer' => $customer->id,
  // In the latest version of the API, specifying the `automatic_payment_methods` parameter is optional because Stripe enables its functionality by default.
  'automatic_payment_methods' => [
    'enabled' => 'true',
  ],
]);

echo json_encode(
  [
    'paymentIntent' => $paymentIntent->client_secret,
    'ephemeralKey' => $ephemeralKey->secret,
    'customer' => $customer->id,
    'publishableKey' => 'PUT_YOUR_PUBLISHABLE_KEY_HERE'
  ]
);
http_response_code(200);