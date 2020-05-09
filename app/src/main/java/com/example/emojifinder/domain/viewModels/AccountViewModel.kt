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

    var oldEmail : String ?= null
    var oldPassword : String ?= null

    init {
        coroutineScope.launch {
            fetchUserLevelsStatistic()
            fetchMainUserData()
        }
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
            coroutineScope.launch {
                if(!oldEmail.isNullOrEmpty() && !oldPassword.isNullOrEmpty()){
                    userMainData.updateEmailAndPassword(oldEmail!!,oldPassword!!,
                        new_email.text.toString(),new_password.text.toString())
                }
            }
        }
    }

    private suspend fun fetchMainUserData() {
        withContext(Dispatchers.Main){
            _userMainDataResponse.value = Result.Loading
        }

        val user = userMainData.fetchUserMainInfo()

        withContext(Dispatchers.Main){
            _userMainDataResponse.value = user
        }
    }

    private suspend fun fetchUserLevelsStatistic() {
        withContext(Dispatchers.Main){
            _levelsStatisticResponse.value = Result.Loading
        }

        val levels = levelStatistics.fetchUserLevelsStatistic()

        withContext(Dispatchers.Main){
            _levelsStatisticResponse.value = levels
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun statisticResponseComplete() {
        _levelsStatisticResponse.value = null
    }

}