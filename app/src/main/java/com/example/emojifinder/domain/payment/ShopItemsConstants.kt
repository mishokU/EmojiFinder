package com.example.emojifinder.domain.payment

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*

const val LOAD_PAYMENT_DATA_REQUEST_CODE = 4

const val STARTER_PACK_PRICE = "149.00"
const val STARTER_PACK_ID = "00001"

const val MEDIUM_PACK_PRICE = "249.00"
const val MEDIUM_PACK_ID = "00002"

const val HUGE_PACK_PRICE = "449.00"
const val HUGE_PACK_ID = "00003"

const val FIRST_EMO_PRICE = "349.00"
const val SECOND_EMO_PRICE = "549.00"
const val THIRD_EMO_PRICE = "999.00"

const val CURRENCY_CODE = "RUB"

val SUPPORTED_NETWORKS = arrayListOf(WalletConstants.CARD_NETWORK_OTHER,
    WalletConstants.CARD_NETWORK_VISA,
    WalletConstants.CARD_NETWORK_MASTERCARD)

object GooglePaymentUtils {
    fun createGoogleApiClientForPay(context: Context): PaymentsClient =
        Wallet.getPaymentsClient(context,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .setTheme(WalletConstants.THEME_LIGHT)
                .build())

    fun createPaymentDataRequest(price: String): PaymentDataRequest {
        val transaction = createTransaction(price)
        return generatePaymentRequest(transaction)
    }
}