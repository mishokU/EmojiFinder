package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.mishok.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import com.mishok.emojifinder.data.db.remote.service.FirebaseUserData
import com.mishok.emojifinder.domain.auth.CheckOnValid
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.shop.EmojiShopModel
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

    private val _userValuesResponse = MutableLiveData<Result<AccountValuesModel>>()
    val userValuesResponse : LiveData<Result<AccountValuesModel>>
        get() = _userValuesResponse

    var oldEmail : String ?= null
    var oldPassword : String ?= null

    init {
        fetchUserLevelsStatistic()
        fetchMainUserData()
        fetchUserValues()
        fetchUserEmojis()
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

    fun fetchUserLevelsStatistic() {
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

    fun fetchUserValues() {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _userValuesResponse.value = Result.Loading
            }
            val values = userMainData.fetchUserValues()
            withContext(Dispatchers.Main){
                _userValuesResponse.value = values
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }


    fun addEmoji(emoji: EmojiShopModel?, cost : Int, values : AccountValuesModel) {
        coroutineScope.launch {
            userMainData.addEmoji(emoji)
            userMainData.updateValues(-cost, values)
        }
    }

    fun removeEmoji(emoji: EmojiShopModel, cost : Int,values : AccountValuesModel) {
        coroutineScope.launch {
            userMainData.removeEmoji(emoji)
            userMainData.updateValues(cost, values)
        }
    }

    fun addGeneratedEmoji(generatedEmoji: EmojiShopModel?) {
        if(generatedEmoji != null){
            coroutineScope.launch {
                userMainData.addEmoji(generatedEmoji)
            }
        }
    }

    fun updateUserAvatar(avatar: String) {
        coroutineScope.launch {
            userMainData.updateAvatar(avatar)
        }
    }

    fun updateUserBoxes(boxesCount: Int) {
        coroutineScope.launch {
            userMainData.updateBoxes(boxesCount)
        }
    }

    fun updateUserEmos(emos : Int){
        coroutineScope.launch {
            userMainData.updateEmos(emos)
        }
    }

    fun updateScore(score: Int) {
        coroutineScope.launch {
            userMainData.updateScore(score)
        }
    }

    fun updateUserEmojis(emojisCount: Int) {
        coroutineScope.launch {
            userMainData.updateEmojis(emojisCount)
        }
    }
}

