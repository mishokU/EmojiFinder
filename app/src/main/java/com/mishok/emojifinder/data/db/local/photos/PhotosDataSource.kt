package com.mishok.emojifinder.data.db.local.photos

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.constructor.choosePhoto.PhotoPickerModel

class PhotosDataSource {

    private val listOfAllImages = mutableListOf<PhotoPickerModel>()

    private fun getPhotoMediaCursorFromGallery(context: Context): Cursor? {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE
        )

        val selection =
            (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        val queryUri = MediaStore.Files.getContentUri("external")
        val contentResolver: ContentResolver = context.contentResolver
        return contentResolver.query(
            queryUri,
            projection,
            selection,
            null,  // Selection args (none).
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        )
    }

    fun onLoadFinished(context: Context): Result<MutableList<PhotoPickerModel>> {
        return try {
            val cursor = getPhotoMediaCursorFromGallery(context)
            val thumbnailColumnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val imageID = cursor.getInt(thumbnailColumnIndex)
                val path = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "" + imageID
                )
                listOfAllImages.add(PhotoPickerModel(cursor.position, path))
            }
            Result.Success(listOfAllImages)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }
}