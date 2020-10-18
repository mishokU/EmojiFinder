package com.mishok.emojifinder.domain.payment

import android.os.Build
import androidx.annotation.RequiresApi
import com.mishok.emojifinder.domain.wallet.Constants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

val CENTS_IN_A_UNIT = BigDecimal(100.0)

//Minimal version of the pay method
fun getBaseRequest(): JSONObject {
    return JSONObject()
        .put("apiVersion", 2)
        .put("apiVersionMinor", 0)
}

//For card encryption need gateway of master card and my ID
fun getGatewayTokenizationSpecification(): JSONObject? {
    return object : JSONObject() {
        init {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", object : JSONObject() {
                init {
                    put("gateway", "example")
                    put("gatewayMerchantId", "exampleGatewayMerchantId")
                }
            })
        }
    }
}

fun getAllowedCardNetworks() : JSONArray? {
    return object : JSONArray() {
        init {
            put("AMEX")
                put("DISCOVER")
                put("INTERAC")
                put("JCB")
                put("MASTERCARD")
                put("VISA")
        }
    }
}

fun getAllowedCardAuthMethods(): JSONArray? {
    return JSONArray()
        .put("PAN_ONLY")
        .put("CRYPTOGRAM_3DS")
}

@Throws(JSONException::class)
fun getBaseCardPaymentMethod(): JSONObject? {

    val billingAddressParameters = JSONObject().apply {
        put("format", "FULL")
    }

    val parameters = JSONObject().apply {
        put("allowedAuthMethods", getAllowedCardAuthMethods())
        put("allowedCardNetworks", getAllowedCardNetworks())
        // Optionally, you can add billing address/phone number associated with a CARD payment method.
        put("billingAddressRequired", true)
        put("billingAddressParameters", billingAddressParameters)
    }

    return JSONObject().apply {
        put("type", "CARD")
        put("parameters", parameters)
    }
}

@Throws(JSONException::class)
fun getCardPaymentMethod() = getBaseCardPaymentMethod()?.apply  {
    put("tokenizationSpecification", getGatewayTokenizationSpecification())
}


fun getTransactionInfo(price: String?) = JSONObject().apply {
    put("totalPrice", price)
    put("totalPriceStatus", "FINAL")
    put("countryCode", Constants.COUNTRY_CODE)
    put("currencyCode", Constants.CURRENCY_CODE)
}

fun getMerchantInfo() = JSONObject().apply {
    put("merchantName", "Example Merchant")
}

@RequiresApi(Build.VERSION_CODES.N)
fun getPaymentDataRequest(priceLong: Long): Optional<JSONObject> {
    return try {

        val price : String? = centsToString(priceLong)

        val paymentDataRequest: JSONObject = getBaseRequest().apply {
            put("allowedPaymentMethods", JSONArray().put(getCardPaymentMethod()));
            put("transactionInfo", getTransactionInfo(price))
            put("merchantInfo", getMerchantInfo())
        }
        Optional.of(paymentDataRequest)
    } catch (e: JSONException) {
        Optional.empty()
    }
}

fun centsToString(cents: Long): String? {
    return BigDecimal(cents)
        .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
        .setScale(2, RoundingMode.HALF_EVEN)
        .toString()
}
