package com.anonymous.prohiking.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anonymous.prohiking.ui.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val currentUser by profileViewModel.currentUser.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            currentUser?.let { user ->
                UserDetails(user.username, user.email)
            }

            MenuItem(
                icon = Icons.Outlined.Person,
                title = "Account",
                description = "Manage your account"
            ) {
                navController.navigate(Screen.Main.Account.route)
            }

            MenuItem(
                icon = Icons.Outlined.Explore,
                title = "Statistics",
                description = "Check out your stats"
            ) {
                navController.navigate(Screen.Main.Statistics.route)
            }

            MenuItem(
                icon = Icons.Outlined.ContactPage,
                title = "Contact",
                description = "Want to contact us?"
            ) {
                navController.navigate(Screen.Main.Contact.route)
            }
        }
    }
}

@Composable
private fun UserDetails(username: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .size(100.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Column(
            modifier = Modifier
                .weight(weight = 3f, fill = false)
                .padding(16.dp)
        ) {
            Text(
                text = username,
                style = TextStyle(
                    fontSize = 22.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = email,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Gray,
                    letterSpacing = (0.8).sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MenuItem(icon: ImageVector, title: String, description: String, callback: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                callback()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primaryContainer
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        fontFamily = FontFamily.Default,
                        color = Color.Gray
                    )
                )
            }

            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = title,
                tint = Color.Black.copy(alpha = 0.70f)
            )


        }
    }
}
