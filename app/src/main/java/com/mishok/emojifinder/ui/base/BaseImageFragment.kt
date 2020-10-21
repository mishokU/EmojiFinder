package com.mishok.emojifinder.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.mishok.emojifinder.ui.constructor.dialogs.SentLevelDialog
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment

open class BaseImageFragment : DaggerFragment() {

    protected fun pickPhoto(code: Int) {
        val takePicture = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, IMAGE_PICK_CODE)
    }

    protected fun pickImageFromGallery(code: Int) {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(pickPhoto, PICK_PHOTO)
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
        if (resultCode != DaggerAppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    SentLevelDialog.setImage(data?.data)
                }
                PICK_PHOTO -> {

                }
            }
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        private const val PICK_PHOTO = 2000

        const val SAVE_CODE = 100
        const val SENT_CODE = 101

        //Permission code
        private const val PERMISSION_CODE = 1001
    }
}