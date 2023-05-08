package com.anonymous.prohiking.ui.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.MainActivity
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.theme.md_theme_light_primaryContainer
import com.anonymous.prohiking.ui.widgets.CustomTextField
import com.anonymous.prohiking.ui.widgets.LoadingAnimation

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp)
        )

        when (val uiState = loginViewModel.uiState.collectAsState().value) {
            is LoginUiState.Loading -> LoadingScreen()
            is LoginUiState.LoggedOut -> LoggedOutScreen(navController, uiState, loginViewModel)
            is LoginUiState.LoggedIn -> context.startActivity(
                Intent(
                    context,
                    MainActivity::class.java
                )
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation()
    }
}

@Composable
private fun LoggedOutScreen(
    navController: NavController,
    uiState: LoginUiState.LoggedOut,
    loginViewModel: LoginViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .alpha(0.6f)
                .clip(
                    CutCornerShape(
                        topStart = 8.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Welcome ProHiker", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

                Text(
                    text = "Sign in to continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column {
                CustomTextField(
                    value = uiState.usernameText,
                    label = "Username",
                    placeholder = "Enter your email address",
                    onValueChange = {
                        loginViewModel.updateUsernameText(it)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = uiState.passwordText,
                    label = "Password",
                    placeholder = "Enter your Password",
                    onValueChange = {
                        loginViewModel.updatePasswordText(it)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Password")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    )
                )

                TextButton(
                    onClick = {
                        // forgot password
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password", color = md_theme_light_primaryContainer)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { loginViewModel.onLoginButtonClick() },
                    colors = ButtonDefaults.buttonColors(md_theme_light_primaryContainer)
                ) {
                    Text(text = "Log In")
                }

                TextButton(onClick = { navController.navigate(Screen.Auth.Register.route) }) {
                    Text(
                        "Don't have an account, click here",
                        color = md_theme_light_primaryContainer
                    )
                }
            }
        }
    }
}