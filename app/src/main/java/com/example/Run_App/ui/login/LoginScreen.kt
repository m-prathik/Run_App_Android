package com.example.Run_App.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.Run_App.Navigation.Screen
import com.example.Run_App.ui.login.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current

    val firstRequester = remember{FocusRequester()}
    val secondRequester = remember{FocusRequester()}
    var isPasswordFieldEmpty by remember{ mutableStateOf(false)}
    var passwordVisible by remember{mutableStateOf(false)}
    val iconColor = if(isSystemInDarkTheme()) Color.White else Color.Black
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    val loginResult by viewModel.loginResult.observeAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    }
                    )
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Run App",
                color = MaterialTheme.colors.primary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Please Login",
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace

            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                modifier = Modifier.focusRequester(firstRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red
                ),
                label = { Text(text = "Email Address",color = Color.Gray)},
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        secondRequester.requestFocus()
                    }
                )
            )


            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it
                                    if(isPasswordFieldEmpty && it.isBlank()) {
                                        isPasswordFieldEmpty = false
                                    }
                                },
                singleLine = true,
                modifier = Modifier.focusRequester(secondRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = Color.Red
                ),
                label = { Text(text = "Password",color = Color.Gray) },
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                visualTransformation = if (passwordVisible)  VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if(passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val description = if(passwordVisible) "Hide Password" else "Show Password"
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            imageVector = icon,
                            contentDescription = description,
                            tint = iconColor
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // add code to check for blank field and make field red
                        isPasswordFieldEmpty = password.isEmpty()
                        if(isPasswordFieldEmpty) {

                        }
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Login Button
            Button(onClick = {
                viewModel.login(email, password)
            },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray
                    ),
                modifier = Modifier.width(200.dp).height(50.dp).border(2.dp, Color.Black)) {
                Text(
                    text = "Login",
                    color = Color.LightGray,
                )
            }

            loginResult?.let { result ->
                when {
                    result.isSuccess -> {
                        LaunchedEffect(Unit) {
                            navController.navigate(Screen.QuickRun.route) {
                                popUpTo(Screen.Home.route) {inclusive = true}
                            }
                        }
                    }
                    result.isFailure ->Text("Login Failed:${result.exceptionOrNull()?.message}")
//                    result.isFailure -> {
//                        LaunchedEffect(Unit) {
//                            navController.navigate(Screen.GuidedRun.route) {
//                                popUpTo(Screen.Home.route) {inclusive = true}
//                            }
//                        }
//                    }
                }
            }

            
        }

}