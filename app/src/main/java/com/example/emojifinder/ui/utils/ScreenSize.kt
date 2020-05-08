@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.example.emojifinder.ui.utils

import android.content.res.Configuration
import android.content.res.Resources
import com.example.emojifinder.R
import com.example.emojifinder.data.db.remote.models.EmojiModel

object ScreenSize {

    fun getEmojiSize(
        resources: Resources
    ) : Float {
        return when {
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                resources.getDimension(R.dimen.emoji_huge_font)
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                 resources.getDimension(R.dimen.emoji_medium_font)
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_SMALL -> {
               resources.getDimension(R.dimen.emoji_small_font)
            }
            else -> resources.getDimension(R.dimen.emoji_font)
        }
    }

    fun getScreenSize(
        resources: Resources,
        list: List<EmojiModel?>
    ) : Float {
        return when {
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                when(list.size){
                    1 -> resources.getDimension(R.dimen.emoji_small_font_one)
                    in 2..4 -> resources.getDimension(R.dimen.emoji_small_font_two)
                    in 5..9 -> resources.getDimension(R.dimen.emoji_small_font_nine)
                    in 10..16 -> resources.getDimension(R.dimen.emoji_small_font_sixteen)
                    in 17..32 -> resources.getDimension(R.dimen.emoji_small_font_thirty_two)
                    in 33..64 -> resources.getDimension(R.dimen.emoji_small_font_sixty_four)
                    else -> resources.getDimension(R.dimen.emoji_small_font)
                }
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                when(list.size){
                    1 -> resources.getDimension(R.dimen.emoji_small_font_one)
                    in 2..4 -> resources.getDimension(R.dimen.emoji_small_font_two)
                    in 5..9 -> resources.getDimension(R.dimen.emoji_small_font_nine)
                    in 10..16 -> resources.getDimension(R.dimen.emoji_small_font_sixteen)
                    in 17..32 -> resources.getDimension(R.dimen.emoji_small_font_thirty_two)
                    in 33..64 -> resources.getDimension(R.dimen.emoji_small_font_sixty_four)
                    else -> resources.getDimension(R.dimen.emoji_small_font)
                }
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                when(list.size){
                    1 -> resources.getDimension(R.dimen.emoji_small_font_one)
                    in 2..4 -> resources.getDimension(R.dimen.emoji_small_font_two)
                    in 5..9 -> resources.getDimension(R.dimen.emoji_small_font_nine)
                    in 10..16 -> resources.getDimension(R.dimen.emoji_small_font_sixteen)
                    in 17..32 -> resources.getDimension(R.dimen.emoji_small_font_thirty_two)
                    in 33..64 -> resources.getDimension(R.dimen.emoji_small_font_sixty_four)
                    else -> resources.getDimension(R.dimen.emoji_small_font)
                }
            }
            else -> resources.getDimension(R.dimen.emoji_font)
        }
    }
}