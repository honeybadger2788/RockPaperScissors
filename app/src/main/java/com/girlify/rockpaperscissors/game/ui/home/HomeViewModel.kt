package com.girlify.rockpaperscissors.game.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onCheck(username: String) {
        viewModelScope.launch {
            _username.value = username
            _isButtonEnable.value = username.trim().isNotEmpty()
        }
    }

    fun onClick() {
        viewModelScope.launch{
            _showDialog.value = true
        }
    }

    fun onDismiss() {
        viewModelScope.launch{
            _showDialog.value = false
        }
    }
}