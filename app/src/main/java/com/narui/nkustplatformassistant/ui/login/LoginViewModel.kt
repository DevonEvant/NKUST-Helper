package com.narui.nkustplatformassistant.ui.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.*
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// LoginParams State hosting
class LoginParamsViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _uid: MutableLiveData<String> = MutableLiveData("")
    private val _pwd: MutableLiveData<String> = MutableLiveData("")
    private val _pwdVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    val uid: LiveData<String> = _uid
    val pwd: LiveData<String> = _pwd
    val pwdVisibility: LiveData<Boolean> = _pwdVisibility

    fun currentConnectState(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager
            .getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    /**
     * different from [DataRepository.loginState]
     */
    private val _loginState = MutableLiveData(false)
    val loginState: LiveData<Boolean> get() = _loginState

    fun onUidChange(newUid: String) {
        _uid.value = newUid
    }

    fun onPwdChange(newPwd: String) {
        _pwd.value = newPwd
    }

    fun onPwdVisibilityReversed() {
        _pwdVisibility.value = (_pwdVisibility.value)!!.not()
    }

    private val _imageBitmap = MutableLiveData<ImageBitmap>()
    private val _etxtCode: MutableLiveData<String> = MutableLiveData()
    private val _etxtIsLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    val etxtImageBitmap: LiveData<ImageBitmap> = _imageBitmap
    val etxtCode: LiveData<String> = _etxtCode
    val etxtIsLoading: LiveData<Boolean> = _etxtIsLoading

    fun requestEtxtImageBitmap() {
        viewModelScope.launch {
            _etxtIsLoading.value = true
            onEtxtImageBitmapChange(dataRepository.getWebapCaptchaImage(true))
        }
        _etxtIsLoading.value = false
    }

    private fun onEtxtImageBitmapChange(newEtxtImageBitmap: ImageBitmap) {
        _imageBitmap.value = newEtxtImageBitmap
    }

    fun onEtxtCodeChange(newEtxtCode: String) {
        _etxtCode.value = newEtxtCode
    }

    // TODO: rewrite it to other method of Coroutine scope
    fun loginForResult() = liveData<Boolean> {
        emit(dataRepository.userLogin(uid.value!!, pwd.value!!, etxtCode.value!!)
            .let {
                println("Login state: $it")
                it
            }
        )
    }
}