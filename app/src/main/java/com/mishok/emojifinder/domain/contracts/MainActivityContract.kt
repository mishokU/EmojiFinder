package com.mishok.emojifinder.domain.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.mishok.emojifinder.ui.main.MainActivity

class MainActivityContract: ActivityResultContract<String, Intent?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, MainActivity::class.java)
            .putExtra("my_input_key", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent
    }

}