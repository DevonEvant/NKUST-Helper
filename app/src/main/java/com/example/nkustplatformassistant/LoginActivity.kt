package com.example.nkustplatformassistant

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            Nkust_platform_assistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginForm()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginActivityPreview() {
    Nkust_platform_assistantTheme {
        LoginForm()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginForm() {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var uid by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var pwdVisibility by rememberSaveable { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialogForEtxtCode(
            showDialog = showDialog.value,
            onPositiveClick = { etxtCode ->
                showDialog.value = false
//                user.loginWebap(uid, pwd, etxtCode)
            },
            onNegativeClick = {
                showDialog.value = false
                Toast.makeText(context, "Enter validate code before login!", Toast.LENGTH_LONG)
                    .show()
            }) // TODO: coroutineScopeState = true, Pop to MainActivity
    }
    Column(
        modifier = Modifier
            .padding(start = 10.dp, bottom = 10.dp)
            // TODO: pointerInput() broken
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        OutlinedTextField(
            value = uid,
            onValueChange = { uid = it },
            label = { Text(text = "Student ID") },
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.TwoTone.Person,
                    "")
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .focusTarget()
                .fillMaxWidth(0.85F),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
            )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.85F),
            value = pwd,
            onValueChange = { pwd = it },
            label = { Text(text = "Password") },
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
                    // TODO: Course Activity
                }
            ), trailingIcon = {
                val image =
                    if (pwdVisibility) Icons.TwoTone.Visibility
                    else Icons.TwoTone.VisibilityOff
                val description =
                    if (pwdVisibility) "Hide password"
                    else "Show password"
                IconButton(onClick = { pwdVisibility = !pwdVisibility }) {
                    Icon(imageVector = image, description)
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
                            "ID or PW didn't input",
                            Toast.LENGTH_LONG).show()
                    } else {
                        showDialog.value = true
                    }
                },
                shape = RoundedCornerShape(50),
            ) {
                Text(text = "Login")
                Icon(imageVector = Icons.TwoTone.Login,
                    contentDescription = null,
                    Modifier.padding(start = 10.dp))
            }
        }
    }
}

class EtxtCodeViewModel() : ViewModel() {
    private val _imageBitmap = MutableLiveData<ImageBitmap>()
    val imageBitmap: LiveData<ImageBitmap> get() = _imageBitmap

    init {
        getImageBitmap()
    }

    private fun getImageBitmap() {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.Main)
        {
            val bitmap = user.getWebapEtxtBitmap()
            _imageBitmap.value = bitmap
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun AlertDialogForEtxtCode(
    showDialog: Boolean,
    onPositiveClick: (String) -> Unit,
    onNegativeClick: () -> Unit,
) {
    val viewModel = EtxtCodeViewModel()
    val bitmap = remember { mutableStateOf(viewModel.imageBitmap.value) }
    var etxtCode by remember { mutableStateOf("") }
    viewModel.imageBitmap.observe(LocalLifecycleOwner.current, Observer {
        bitmap.value = it
    })
    // https://stackoverflow.com/questions/68639232/jetpack-compose-how-to-create-an-imagebitmap-with-specific-size-and-configuratio

    if (showDialog) {
        Dialog(onDismissRequest = {}) {
            OutlinedCard(
                elevation = CardDefaults.outlinedCardElevation(15.dp),
                modifier = Modifier.aspectRatio(1F))
            {
                if (bitmap.value != null) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Please input validate code below:", fontSize = 20.sp)
                        Spacer(modifier = Modifier.padding(10.dp))

                        Image(
                            bitmap = bitmap.value!!,
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier.aspectRatio(5F),
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(

                            value = etxtCode,
                            onValueChange = { etxtCode = it },
                            label = { Text(text = "Validate Code") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(imageVector = Icons.TwoTone.HowToReg,
                                    "")
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .focusTarget()
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                            )
                        Spacer(modifier = Modifier.padding(5.dp))
                        //button
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(onClick = {
                                onNegativeClick.invoke()
                            }) {
                                Icon(imageVector = Icons.TwoTone.Cancel,
                                    modifier = Modifier.padding(end = 4.dp),
                                    contentDescription = null)
                                Text(text = "Cancel login")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            OutlinedButton(onClick = {
                                onPositiveClick.invoke(etxtCode)
                            }, modifier = Modifier.padding(end = 4.dp)) {
                                Icon(imageVector = Icons.TwoTone.Verified,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp))
                                Text(text = "Login")
                            }
                        }
                    }
                } else {
                    Column() {
                        Text(text = "Please wait...", fontSize = 20.sp)
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}