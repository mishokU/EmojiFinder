@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.mishok.emojifinder.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import com.mishok.emojifinder.R
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel

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
        list: List<EmojiShopModel?>
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

    fun getScreenSizeAll(
        resources: Resources,
        list: List<com.mishok.emojifinder.ui.shop.EmojiShopModel?>
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

    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}