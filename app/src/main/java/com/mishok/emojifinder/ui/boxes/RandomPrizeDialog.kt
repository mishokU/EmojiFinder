package com.mishok.emojifinder.ui.boxes

import androidx.appcompat.widget.AppCompatImageView
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import dagger.android.support.DaggerFragment

object RandomPrizeDialog {

    lateinit var dialogView: BaseDialog
    lateinit var fragment: DaggerFragment

    private lateinit var secondEmoji: EmojiAppCompatEditText
    private lateinit var secondEmojiPlaceholder: AppCompatImageView
    private lateinit var firstEmoji: EmojiAppCompatEditText
    private lateinit var firstEmojiPlaceholder: AppCompatImageView

    var emojis: MutableList<EmojiShopModel> = mutableListOf()

    private val _emoji = MutableLiveData<EmojiShopModel>()
    val emoji: LiveData<EmojiShopModel>
        get() = _emoji

    private var randomEmojis: List<EmojiShopModel> = emptyList()

    fun create(fragment: DaggerFragment, randomEmojis: List<EmojiShopModel>) {
        this.fragment = fragment
        this.randomEmojis = randomEmojis
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.random_prize_dialog)

        setRandomEmojis()
    }

    private fun initEmojis() {
        initFirstPrize()
        initSecondEmoji()
    }

    private fun initSecondEmoji() {
        secondEmoji = dialogView.findViewById(R.id.second_chest_emoji)
        secondEmojiPlaceholder = dialogView.findViewById(R.id.second_emoji_placeholder)
        secondEmojiPlaceholder.setOnClickListener {
            _emoji.value = emojis[1]
            secondEmojiPlaceholderAnimate()
            secondEmojiAnimate(true)
        }
    }

    private fun secondEmojiAnimate(click: Boolean) {
        secondEmoji
            .animate()
            .alpha(1.0f)
            .setDuration(1500)
            .withEndAction {
                if (click) {
                    firstEmojiPlaceholderAnimate()
                    firstEmojiAnimate(false)
                } else {
                    hideSecondPlaceholder()
                }
            }
            .start()
    }

    private fun hideSecondPlaceholder() {
        secondEmojiPlaceholder
            .animate()
            .alpha(1f)
            .setDuration(1500)
            .withEndAction {
                hide()
            }
            .start()
    }

    private fun secondEmojiPlaceholderAnimate() {
        secondEmojiPlaceholder.animate()
            .alpha(0f)
            .setDuration(1500)
            .start()
    }

    private fun initFirstPrize() {
        firstEmoji = dialogView.findViewById(R.id.first_chest_emoji)
        firstEmojiPlaceholder = dialogView.findViewById(R.id.first_emoji_placeholder)
        firstEmojiPlaceholder.setOnClickListener {
            _emoji.value = emojis[0]
            firstEmojiPlaceholderAnimate()
            firstEmojiAnimate(true)
        }
    }

    private fun firstEmojiAnimate(click: Boolean) {
        firstEmoji
            .animate()
            .alpha(1.0f)
            .setDuration(1500)
            .withEndAction {
                if (click) {
                    secondEmojiPlaceholderAnimate()
                    secondEmojiAnimate(false)
                } else {
                    hideFirstPlaceholder()
                }
            }
            .start()
    }

    private fun hideFirstPlaceholder() {
        firstEmojiPlaceholder
            .animate()
            .alpha(1f)
            .setDuration(1500)
            .withEndAction {
                hide()
            }
            .start()
    }

    private fun firstEmojiPlaceholderAnimate() {
        firstEmojiPlaceholder.animate()
            .alpha(0f)
            .setDuration(1500)
            .start()
    }

    fun show() {
        dialogView.show()
    }

    private fun hide() {
        emojis.removeAll(emojis)
        dialogView.dismiss()
    }

    private fun setRandomEmojis() {
        emojis.add(randomEmojis.random())
        emojis.add(randomEmojis.random())
        initEmojis()
        secondEmoji.setText(emojis[0].text)
        firstEmoji.setText(emojis[1].text)
    }

}