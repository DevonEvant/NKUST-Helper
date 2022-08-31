@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun LoginScreen(viewmodel: LoginParamsViewModel) {
    LoginScreenBase(viewmodel)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
    }
    val loginParamsViewModel: LoginParamsViewModel =
        viewModel(viewModelStoreOwner = viewModelStoreOwner)
    Nkust_platform_assistantTheme {
        LoginScreenBase(loginParamsViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginWhateverPreview() {
    Nkust_platform_assistantTheme() {
        Column() {
            ShowDialogBase(showDialog = remember { mutableStateOf(true) })
        }
    }
}
