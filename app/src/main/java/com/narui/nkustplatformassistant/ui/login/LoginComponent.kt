package com.narui.nkustplatformassistant.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.narui.nkustplatformassistant.R
import com.narui.nkustplatformassistant.navigation.Screen
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun LoginScreenBase(loginParamsViewModel: LoginParamsViewModel, navController: NavController) {
    val uid: String by loginParamsViewModel.uid.observeAsState("")
    val pwd: String by loginParamsViewModel.pwd.observeAsState("")
    val pwdVisibility: Boolean by loginParamsViewModel.pwdVisibility.observeAsState(false)

    val context = LocalContext.current

    // https://proandroiddev.com/how-to-collect-flows-lifecycle-aware-in-jetpack-compose-babd53582d0b
    val loginState by loginParamsViewModel.loginState.observeAsState(false)

    LaunchedEffect(loginState) {
        fun showToast(value: String) {
            Toast.makeText(context,
                context.getString(R.string.login_toast_loginstate, value),
                Toast.LENGTH_SHORT)
                .show()
        }
        when (loginState) {
            true -> {
                showToast(context.getString(R.string.login_toast_loginsucceed))
                navController.navigate(Screen.Home.route)
            }
            false -> {
                delay(3500L)
                showToast(context.getString(R.string.login_toast_loginfail))
            }
        }
    }


    // To pass viewModel between composable, we use viewModelStoreOwner and
    // add a loginParamsViewModel4 to showDialogBase
    LoginForm(
        uid = uid, pwd = pwd, pwdVisibility = pwdVisibility,
        onUidChanged = { loginParamsViewModel.onUidChange(it) },
        onPwdChanged = { loginParamsViewModel.onPwdChange(it) },
        onPwdVisibilityReversed = { loginParamsViewModel.onPwdVisibilityReversed() },
        loginParamsViewModel = loginParamsViewModel,
    )

    LaunchedEffect(Unit) {
        delay(3500L)
        Toast.makeText(context,
            context.getString(R.string.login_toast_database),
            Toast.LENGTH_LONG).show()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginForm(
    uid: String,
    pwd: String,
    pwdVisibility: Boolean,
    onUidChanged: (String) -> Unit,
    onPwdChanged: (String) -> Unit,
    onPwdVisibilityReversed: () -> Unit,
    loginParamsViewModel: LoginParamsViewModel,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    ShowDialogBase(
        showDialog = showDialog,
        loginParamsViewModel = loginParamsViewModel,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.login_welcome),
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        OutlinedTextField(
            value = uid.uppercase(),
            onValueChange = onUidChanged,
            label = { Text(stringResource(R.string.login_student_id)) },
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.TwoTone.Person,
                    "")
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .focusTarget()
                .fillMaxWidth(0.85F),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go,
                capitalization = KeyboardCapitalization.Characters),
            keyboardActions = KeyboardActions(
                onGo = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
            )
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.85F),
            value = pwd,
            onValueChange = onPwdChanged,
            label = { Text(stringResource(R.string.login_student_password)) },
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.TwoTone.Password,
                    "")
            },
            shape = RoundedCornerShape(50),
            visualTransformation = if (pwdVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    // TODO: If auto etxt implement is complete
                    //       then check and login here
                }
            ), trailingIcon = {
                val image =
                    if (pwdVisibility) Icons.TwoTone.Visibility
                    else Icons.TwoTone.VisibilityOff
                IconButton(onClick = onPwdVisibilityReversed) {
                    Icon(imageVector = image, null)
                }
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // TODO: Check if the user entered uid and pwd using better way
                    if (pwd.isEmpty() || uid.isEmpty()) {
                        Toast.makeText(context,
                            context.getString(R.string.login_textfield_noinput),
                            Toast.LENGTH_LONG).show()
                    } else {
                        if (loginParamsViewModel.currentConnectState(context)) {
                            showDialog.value = true
                        } else {
                            Toast.makeText(context,
                                context.getString(R.string.login_network_failed),
                                Toast.LENGTH_LONG).show()
                        }
                    }
                },
                shape = RoundedCornerShape(50),
            ) {
                Text(stringResource(R.string.login_login))
                Icon(imageVector = Icons.TwoTone.Login,
                    contentDescription = null,
                    Modifier.padding(start = 10.dp))
            }
        }
    }
}

@Composable
fun ShowDialogBase(
    showDialog: MutableState<Boolean>,
    context: Context = LocalContext.current,
    loginParamsViewModel: LoginParamsViewModel,
) {

    val etxtCode: String by loginParamsViewModel.etxtCode.observeAsState("")
    val etxtImageBitmap: ImageBitmap by loginParamsViewModel.etxtImageBitmap
        .observeAsState(ImageBitmap(width = 85, height = 40))


    fun onImageClicked() {
        loginParamsViewModel.requestEtxtImageBitmap()
    }

    fun onPositiveCallback() {
        when {
            etxtCode.length < 4 -> {
                Toast.makeText(context,
                    context.getString(R.string.login_textfield_etxtfailed),
                    Toast.LENGTH_LONG)
                    .show()
            }
            etxtCode.isEmpty() -> {
                Toast.makeText(context,
                    context.getString(R.string.login_textfield_etxtnoinput),
                    Toast.LENGTH_LONG)
                    .show()
            }
            etxtCode.length == 4 -> {
                Toast.makeText(context,
                    context.getString(R.string.login_textfield_etxtchecking),
                    Toast.LENGTH_SHORT)
                    .show()

                // TODO: Login! and start new intent
                // Redirect to Home Page and Start fetching data to
                // DB in HomeScreen

                loginParamsViewModel.loginForResult()

                showDialog.value = false
            }
        }
    }


    fun onNegativeCallback() {
        showDialog.value = false
    }

    if (showDialog.value) {
        LaunchedEffect(showDialog.value) {
            loginParamsViewModel.requestEtxtImageBitmap()
        }

        AlertDialogForEtxtCode(
            etxtCode = etxtCode,
            etxtImageBitmap = etxtImageBitmap,
            etxtIsLoading = loginParamsViewModel.etxtIsLoading,
            onEtxtCodeChange = { loginParamsViewModel.onEtxtCodeChange(it) },
            onImageClicked = { onImageClicked() },
            onPositiveClick = { onPositiveCallback() },
            onNegativeClick = { onNegativeCallback() },
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun AlertDialogForEtxtCode(
    etxtIsLoading: LiveData<Boolean>,
    etxtCode: String,
    etxtImageBitmap: ImageBitmap,
    onEtxtCodeChange: (String) -> Unit,
    onImageClicked: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    // https://stackoverflow.com/questions/68639232/jetpack-compose-how-to-create-an-imagebitmap-with-specific-size-and-configuratio
    val etxtIsLoadingValue = etxtIsLoading.value!!
    Dialog(onDismissRequest = {}) {
        OutlinedCard(
            elevation = CardDefaults.outlinedCardElevation(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    stringResource(R.string.login_validate_code_message),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.padding(10.dp))

                Box(modifier = Modifier.size(width = 200.dp, height = 100.dp),
                    contentAlignment = Alignment.Center) {
                    if (etxtIsLoadingValue)
                        CircularProgressIndicator()
                    else
                        Image(
                            bitmap = etxtImageBitmap,
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { onImageClicked.invoke() },
                            contentScale = ContentScale.FillWidth
                        )
                }

                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = etxtCode.uppercase(),
                    onValueChange = { onEtxtCodeChange(it) },
                    label = { Text(stringResource(R.string.login_validate_code)) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.TwoTone.HowToReg,
                            "")
                    },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .focusTarget()
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        capitalization = KeyboardCapitalization.Characters),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onPositiveClick.invoke()
                    })
                )
                Spacer(modifier = Modifier.padding(5.dp))
                //button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        onNegativeClick.invoke()
                        focusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.TwoTone.Cancel,
                            modifier = Modifier.padding(end = 4.dp),
                            contentDescription = null)
                        Text(stringResource(R.string.login_cancel))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedButton(
                        onClick = {
                            onPositiveClick.invoke()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(end = 4.dp)) {
                        Icon(imageVector = Icons.TwoTone.Verified,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp))
                        Text(stringResource(R.string.login_proceed))
                    }
                }
            }
        }
    }
}