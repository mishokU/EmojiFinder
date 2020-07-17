package com.example.emojifinder.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.example.emojifinder.ui.constructor.dialogs.SentLevelDialog
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment

open class BaseImageFragment : DaggerFragment() {

    protected fun pickImageFromGallery(code : Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery(requestCode)
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == DaggerAppCompatActivity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            SentLevelDialog.setImage(data?.data)
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        const val SAVE_CODE = 100
        const val SENT_CODE = 101
        //Permission code
        private const val PERMISSION_CODE = 1001
    }
}