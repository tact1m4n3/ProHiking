package com.anonymous.prohiking.ui.main

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.StartActivity

@Composable
fun AccountScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    val context = LocalContext.current
    val currentUser by profileViewModel.currentUser.collectAsState()

    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.onPrimaryContainer)) {
        Image(
            painter = painterResource(id = R.drawable.lovepik_com_400203927_green_forest),
            contentDescription = "Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }

                Text(
                    text = "Account",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    softWrap = true,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            currentUser?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {

                    Card(
                        elevation = CardDefaults.elevatedCardElevation(),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Username:",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = (0.8).sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color.Gray
                                )
                            )

                            Text(
                                text = user.username,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Default
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Email:",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = (0.8).sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color.Gray
                                )
                            )

                            Text(
                                text = user.email,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Default
                                )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            shape = CircleShape,
                            onClick = {
                                profileViewModel.onLogoutButtonPressed()
                                context.startActivity(
                                    Intent(
                                        context,
                                        StartActivity::class.java
                                    )
                                )
                            },
                        ) {
                            Text(
                                "Log out",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}


