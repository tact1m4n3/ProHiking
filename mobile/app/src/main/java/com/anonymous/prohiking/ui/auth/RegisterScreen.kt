package com.anonymous.prohiking.ui.auth

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.Screen
import com.anonymous.prohiking.ui.theme.md_theme_light_primaryContainer

@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Box(modifier= Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.background),
            contentDescription ="Login",
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
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
            Modifier
                .fillMaxSize()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround) {
            RegisterHeader()
            RegisterFields(username,
                password,
                onUsernameChange = {
                    username = it
                },
                onPasswordChange = {
                    password=it
                }
            )
            RegisterFooter(
                onSignUpClick = {},
                onAlreadyHaveAccount = {
                    navController.navigate(Screen.Auth.Login.route)
                }
            )

        }
    }
}
@Composable
fun RegisterHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Welcome ProHiker", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
    }
}
@Composable
fun RegisterFields(username: String,
                password: String,
                onUsernameChange: (String)-> Unit,
                onPasswordChange: (String)-> Unit)
               
{
    Column{
        DemoFields(value = username,
            label = "Email",
            placeholder = "Enter your email address",
            onValueChange = onUsernameChange,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next )
        )

        Spacer(modifier= Modifier.height(8.dp))

        DemoFields(value = password,
            label = "Password" ,
            placeholder = "Enter your Password",
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")

            } ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go )
        )

        DemoFields(value = password,
            label = "Verify your password" ,
            placeholder = "Enter your Password",
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")

            } ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go )
        )


    }


}

@Composable
fun RegisterFooter(

    onSignUpClick: ()-> Unit,
    onAlreadyHaveAccount: ()->Unit

) {
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Button(onClick = { onSignUpClick() }, Modifier.fillMaxWidth(), colors= ButtonDefaults.buttonColors(md_theme_light_primaryContainer) ) {
            Text(text= "Sign up")
        }

        TextButton(onClick = { onAlreadyHaveAccount() }) {
            Text("Already have an account", color = md_theme_light_primaryContainer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoFields(value: String,
              label: String,
              placeholder: String,
              visualTransformation: VisualTransformation= VisualTransformation.None ,
              keyboardOptions: KeyboardOptions= KeyboardOptions.Default,
              leadingIcon: @Composable (() -> Unit)?=null,
              trailingIcon: @Composable (() -> Unit)?=null,
              onValueChange: (String)-> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label= {
            Text(text= label)
        },
        placeholder= {
            Text(text = placeholder)
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

