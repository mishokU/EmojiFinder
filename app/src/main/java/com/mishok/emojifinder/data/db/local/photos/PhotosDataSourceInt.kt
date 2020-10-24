package com.mishok.emojifinder.data.db.local.photos

import android.database.Cursor
import android.os.Bundle
import androidx.loader.content.Loader

interface PhotosDataSourceInt {
    fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor>
    fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?): MutableList<String>
}