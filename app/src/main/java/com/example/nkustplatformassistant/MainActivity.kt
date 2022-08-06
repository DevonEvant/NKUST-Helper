package com.example.nkustplatformassistant

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

val user = NkustUser()


/**
 * Interface to the Login data layer.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nkust_platform_assistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginForm()

                    lateinit var imgBitmap: Bitmap
                    val coroutineScope = rememberCoroutineScope()
                    val getimgBitmap: () -> Unit = {
                        coroutineScope.launch {
                            println("0==============================")
                            imgBitmap = BitmapFactory.decodeFile(user.getWebapEtxtImg().path)
                        }
                    }

                    getimgBitmap()

                    fun sent(etxt: String) {
                        if (etxt.length == 4)
                            coroutineScope.launch {
                                user.loginWebap("C110152351", "c110ankust", etxt)
                                println(user.checkLoginValid().toString())
                            }
                    }

                    var w: String by remember {
                        mutableStateOf("")
                    }

//                    Image(bitmap = imgBitmap!!.asImageBitmap(), contentDescription = "123")
                    TextField(value = w, onValueChange = {
                        w = it
                        sent(it)
                    })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Nkust_platform_assistantTheme {
        LoginForm()
//        Greeting("Android111111")
    }
}

/* Reference:
 * https://stackoverflow.com/questions/67897100/jetpack-compose-textfield-capture-keyboard-enter-input
 * https://stackoverflow.com/questions/65304229/toggle-password-field-jetpack-compose
 * https://medium.com/google-developer-experts/focus-in-jetpack-compose-6584252257fe
 * TODO: TextField Style improvement
 * https://pratikchauhan11.medium.com/playing-with-textfield-in-compose-android-declarative-ui-de8c03aa4748
 */

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
            onDismiss = { showDialog.value = false })// TODO: coroutineScopeState = true
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
        Row(
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp)
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
                shape = RoundedCornerShape(50)) {
                Text(text = "Login")
                Icon(imageVector = Icons.TwoTone.Login,
                    contentDescription = "",
                    Modifier.padding(start = 10.dp))
            }
//            Button(
//                onClick = { },
//                shape = RoundedCornerShape(50),
//                colors = ButtonDefaults.textButtonColors()
//
//            ) {
//                Text(text = "Login with WebView")
//                Icon(imageVector = Icons.TwoTone.Login,
//                    contentDescription = "",
//                    Modifier.padding(start = 10.dp))
//            }
        }
    }
}

@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun AlertDialogForEtxtCode(
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var coroutineScopeState = remember { mutableStateOf(true) }
    lateinit var bitmap: ImageBitmap
    coroutineScope.launch {
        bitmap = user.getWebapEtxtBitmap()
        if (!this.isActive){
            coroutineScopeState.value = false
        }
    }
    if (showDialog && !coroutineScopeState.value) {
        Log.e("coroutine", bitmap.toString())
        AlertDialog(
            title = { Text(text = "Please input validate code:") },
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.TwoTone.VerifiedUser,
                        contentDescription = null, Modifier.padding(end = 5.dp))
                    Text(text = "Continue Login")
                }
            },
            // https://stackoverflow.com/questions/68639232/jetpack-compose-how-to-create-an-imagebitmap-with-specific-size-and-configuratio
            text = {
                Image(
                    bitmap = bitmap,
                    contentDescription = null)


            }
        )
    }
}
