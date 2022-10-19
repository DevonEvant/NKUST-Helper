@file:Suppress("SpellCheckingInspection")

package com.narui.nkustplatformassistant.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun LoginScreen(viewModel: LoginParamsViewModel, navController: NavController) {
    LoginScreenBase(viewModel, navController)
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
        LoginScreenBase(
            loginParamsViewModel,
            rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginWhateverPreview() {
    Nkust_platform_assistantTheme {
        val context = LocalContext.current

        Column {
            ShowDialogBase(
                showDialog = remember { mutableStateOf(true) },
                loginParamsViewModel = LoginParamsViewModel(DataRepository(context)),
            )
        }
    }
}
