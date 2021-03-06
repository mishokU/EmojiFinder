package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.data.db.remote.service.FirebaseUsers
import com.mishok.emojifinder.domain.result.Result
import kotlinx.coroutines.*
import javax.inject.Inject

class RatingViewModel @Inject constructor(
    private val firebaseUsers: FirebaseUsers,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _users = MutableLiveData<Result<List<MainAccountModel>>>()
    val users: LiveData<Result<List<MainAccountModel>>>
        get() = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _users.value = Result.Loading
            }
            val users = firebaseUsers.fetchUsers()
            setUserRating(users)
            withContext(Dispatchers.Main) {
                _users.value = users
            }
        }
    }

    private fun setUserRating(users: Result<List<MainAccountModel>>) {
        var place = 1
        when(users){
            is Result.Success -> {
                users.data.forEach {
                    it.place = place
                    place += 1
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}