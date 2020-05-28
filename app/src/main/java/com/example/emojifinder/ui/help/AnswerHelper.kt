package com.example.emojifinder.ui.help

import android.content.Context
import com.example.emojifinder.R
import kotlinx.android.synthetic.main.fragment_help.view.*

object AnswerHelper {

    fun getAnswer(context : Context, question : String) : String{
        val resources = context.resources
        return when(question){
            resources.getString(R.string.how_to_delete_account) -> resources.getString(R.string.how_to_delete_account_answer)
            resources.getString(R.string.help_forgot_password) -> resources.getString(R.string.help_forgot_password_answer)
            resources.getString(R.string.bad_nick_name) -> resources.getString(R.string.bad_nick_name_answer)
            resources.getString(R.string.how_to_play) -> resources.getString(R.string.how_to_play_answer)
            resources.getString(R.string.i_passed_the_level) -> resources.getString(R.string.i_passed_the_level_answer)
            resources.getString(R.string.how_to_disable_adds) -> resources.getString(R.string.how_to_disable_adds_answer)
            else -> resources.getString(R.string.no_answer)
        }
    }
}