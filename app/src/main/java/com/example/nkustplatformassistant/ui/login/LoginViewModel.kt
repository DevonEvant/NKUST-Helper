package com.example.nkustplatformassistant.ui.login

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.remote.FetchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val user = FetchData()

// LoginParams State hosting
class LoginParamsViewModel : ViewModel() {
    private val _uid: MutableLiveData<String> = MutableLiveData("")
    private val _pwd: MutableLiveData<String> = MutableLiveData("")
    private val _pwdVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    val uid: LiveData<String> = _uid
    val pwd: LiveData<String> = _pwd
    val pwdVisibility: LiveData<Boolean> = _pwdVisibility

    fun onUidChange(newUid: String) {
        _uid.value = newUid
    }

    fun onPwdChange(newPwd: String) {
        _pwd.value = newPwd
    }

    fun onPwdVisibilityReversed() {
        _pwdVisibility.value = (_pwdVisibility.value)!!.not()
    }
}

// EtxtCode State Hosting
class EtxtCodeViewModel(
    // Please pass viewModel here or it'll create an new instance!
    private val loginParmsViewModel: LoginParamsViewModel,
) : ViewModel() {
    private val _imageBitmap = MutableLiveData<ImageBitmap>()
    private val _etxtcode: MutableLiveData<String> = MutableLiveData("")

    val etxtImageBitmap: LiveData<ImageBitmap> = _imageBitmap
    val etxtCode: LiveData<String> = _etxtcode

    private var stateOfLogin: Boolean = false

//    init {
//        requestEtxtImageBitmap()
//    }

    fun requestEtxtImageBitmap() {
        viewModelScope.launch {
            onEtxtImageBitmapChange(user.getWebapEtxtBitmap())
        }
    }

    private fun onEtxtImageBitmapChange(newEtxtImageBitmap: ImageBitmap) {
        _imageBitmap.value = newEtxtImageBitmap
    }

    fun onEtxtCodeChange(newEtxtCode: String) {
        _etxtcode.value = newEtxtCode
    }

    // TODO: rewrite it to other method of Corouting scope
    fun loginForResult(): Boolean {
        viewModelScope.launch {
            stateOfLogin = user.loginWebap(
                loginParmsViewModel.uid.value!!,
                loginParmsViewModel.pwd.value!!,
                etxtCode.value!!)
        }
        return stateOfLogin
    }

    fun startFetchData(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DataRepository(context).fetchScoreData()
            }
        }
    }

}