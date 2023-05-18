package com.anonymous.prohiking.ui.start

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.filled.SecurityUpdateGood
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.anonymous.prohiking.data.utils.hasLocationPermission
import com.anonymous.prohiking.ui.MainActivity
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.widgets.CustomTextField
import com.anonymous.prohiking.ui.widgets.LoadingAnimation

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
) {
    val context = LocalContext.current
    val uiState by registerViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp)
        )

        when (uiState) {
            is RegisterUiState.Loading -> LoadingScreen()
            is RegisterUiState.NotRegistered -> NotRegisteredScreen(navController,
                uiState as RegisterUiState.NotRegistered, registerViewModel)
            is RegisterUiState.Registered -> {
                navController.navigate(Screen.Start.Login.route)
                registerViewModel.reset()
            }
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
private fun NotRegisteredScreen(
    navController: NavController,
    uiState: RegisterUiState.NotRegistered,
    registerViewModel: RegisterViewModel
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome ProHiker",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Create a new account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column {
                    CustomTextField(
                        value = uiState.usernameText,
                        label = "Username",
                        placeholder = "Enter your username",
                        onValueChange = {
                            registerViewModel.updateUsernameText(it)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Person4, contentDescription = "username")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = uiState.emailText,
                        label = "Email",
                        placeholder = "Enter your email address",
                        onValueChange = {
                            registerViewModel.updateEmailText(it)
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
                        placeholder = "Enter your password",
                        onValueChange = {
                            registerViewModel.updatePasswordText(it)
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

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = uiState.verifyPasswordText,
                        label = "Verify Password",
                        placeholder = "Enter your password again",
                        onValueChange = {
                            registerViewModel.updateVerifyPasswordText(it)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(Icons.Default.CheckBox, contentDescription = "Password")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Go
                        )
                    )

                    if (uiState.errorMessage.isNotEmpty())
                        Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { registerViewModel.onRegisterButtonClick() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Register")
                    }

                    TextButton(onClick = { navController.navigate(Screen.Start.Login.route) }) {
                        Text(
                            "Already have an account, click here",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
