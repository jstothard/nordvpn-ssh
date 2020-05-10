package com.example.sshnordvpn.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.sshnordvpn.data.LoginRepository
import com.example.sshnordvpn.data.Result

import com.example.sshnordvpn.R
import java.net.Inet4Address

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(ipAddress: String ,username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(ipAddress, username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(ipAddress:String, username: String, password: String) {
        if (!isIpAddressValid(ipAddress)) {
            _loginForm.value = LoginFormState(ipAddressError = R.string.invalid_ip_address)
        }
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // Check if matched IP_ADDRESS format
    private fun isIpAddressValid(ipAddress: String): Boolean {
        return Patterns.IP_ADDRESS.matcher(ipAddress).matches()
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
