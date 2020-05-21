package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import com.example.emojifinder.data.db.remote.service.FirebaseUserData
import com.example.emojifinder.domain.auth.CheckOnValid
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val levelStatistics: FirebaseLevelStatisticImpl,
    private val userMainData : FirebaseUserData,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _levelsStatisticResponse = MutableLiveData<Result<List<UserLevelStatistics?>>>()
    val levelsStatisticResponse : LiveData<Result<List<UserLevelStatistics?>>>
        get() = _levelsStatisticResponse

    private val _userMainDataResponse = MutableLiveData<Result<MainAccountModel?>>()
    val userMainDataResponse : LiveData<Result<MainAccountModel?>>
        get() = _userMainDataResponse

    private val _userEmojisResponse = MutableLiveData<Result<List<EmojiShopModel?>>>()
    val userEmojisResponse : LiveData<Result<List<EmojiShopModel?>>>
        get() = _userEmojisResponse

    var oldEmail : String ?= null
    var oldPassword : String ?= null

    init {
        fetchUserLevelsStatistic()
        fetchMainUserData()
    }

    fun updateLogin(login : TextInputEditText){
        coroutineScope.launch {
            userMainData.updateLogin(login.text.toString())
        }
    }

    fun updateUserEmailAndPassword(
        new_email: TextInputEditText,
        new_password: TextInputEditText
    ) {
        if(CheckOnValid.isEmailValid(new_email) && new_password.text.toString().count() >= 7){
            if(!oldEmail.isNullOrEmpty() && !oldPassword.isNullOrEmpty()){
                coroutineScope.launch {
                    userMainData.updateEmailAndPassword(oldEmail!!,oldPassword!!,
                        new_email.text.toString(),new_password.text.toString())
                }
            }
        }
    }

    fun fetchMainUserData() {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _userMainDataResponse.value = Result.Loading
            }

            val user = userMainData.fetchUserMainInfo()

            withContext(Dispatchers.Main){
                _userMainDataResponse.value = user
            }
        }
    }

    private fun fetchUserLevelsStatistic() {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _levelsStatisticResponse.value = Result.Loading
            }

            val levels = levelStatistics.fetchUserLevelsStatistic()

            withContext(Dispatchers.Main){
                _levelsStatisticResponse.value = levels
            }
        }
    }

    fun fetchUserEmojis(){
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _userEmojisResponse.value = Result.Loading
            }
            val emojis = userMainData.fetchUserEmojis()

            withContext(Dispatchers.Main){
                _userEmojisResponse.value = emojis
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun statisticResponseComplete() {
        _levelsStatisticResponse.value = null
    }

    fun updateUserFullScore(score: Int) {
        coroutineScope.launch {
            userMainData.updateScore(score)
        }
    }

    fun addEmoji(emoji: EmojiShopModel?) {
        coroutineScope.launch {
            userMainData.addEmoji(emoji)
        }
    }

}
