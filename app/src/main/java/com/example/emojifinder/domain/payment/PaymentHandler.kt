package com.example.emojifinder.domain.payment

import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject

val baseCardPaymentMethod = JSONObject().apply {
    put("type", "CARD")
    put("parameters", JSONObject().apply {
        put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
        put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
    })
}

val googlePayBaseConfiguration = JSONObject().apply {
    put("apiVersion", 2)
    put("apiVersionMinor", 0)
    put("allowedPaymentMethods",  JSONArray().put(baseCardPaymentMethod))
}

val tokenizationSpecification = JSONObject().apply {
    put("type", "PAYMENT_GATEWAY")
    put("parameters", JSONObject(mapOf(
        "gateway" to "example",
        "gatewayMerchantId" to "exampleGatewayMerchantId")))
}

val cardPaymentMethod = JSONObject().apply {
    put("type", "CARD")
    put("tokenizationSpecification", tokenizationSpecification)
    put("parameters", JSONObject().apply {
        put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
        put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        put("billingAddressRequired", true)
        put("billingAddressParameters", JSONObject(mapOf("format" to "FULL")))
    })
}

fun createTransaction(price: String): TransactionInfo =
    TransactionInfo.newBuilder()
        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
        .setTotalPrice(price)
        .setCurrencyCode(CURRENCY_CODE)
        .build()

fun generatePaymentRequest(transactionInfo: TransactionInfo): PaymentDataRequest {
    val tokenParams = PaymentMethodTokenizationParameters
        .newBuilder()
        .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_DIRECT)

        .addParameter("publicKey", "0001").build()

    return PaymentDataRequest.newBuilder()
        .setTransactionInfo(transactionInfo)
        .addAllowedPaymentMethod(1)
        .setCardRequirements(
            CardRequirements.newBuilder()
            .addAllowedCardNetworks(SUPPORTED_NETWORKS)
            .setAllowPrepaidCards(true)
            .build())
        .setPaymentMethodTokenizationParameters(tokenParams)
        .build()
}
