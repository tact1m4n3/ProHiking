package com.anonymous.prohiking.ui.main


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.theme.ProHikingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

/*
@Composable
fun ProfileScreen(navController: NavController) {
    //val preferencesRepository = ProHikingApplication.instance.preferencesRepository
    // val username = preferencesRepository.username.collectAsState(initial = "")
    // ${username.value?:"ProHiker"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {


        Column(
            modifier = Modifier
                .padding(36.dp)
                .fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically)

            {
                Text("Welcome back,\nProhiker !", fontSize = 28.sp, lineHeight = 35.sp)
                Spacer(modifier = Modifier.weight(1.0f))
                Card(
                    shape = CircleShape,

                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(100.dp)
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_outline_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize()
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.size(50.dp))
            Row()
            {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.size(10.dp))
                Text("Email:", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.size(50.dp))
            Text("Trails:", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)


            //Contact data
            Column(
                modifier = Modifier
                    .padding(vertical = 150.dp, horizontal = 40.dp)
                    .fillMaxWidth(),
            ) {
                //Text("Dicu Tudor-Andrei\ntudor.andrei.dicu@gmail.com\n+40737387783\n\n" +
                //"Peța Andrei-Mathias\nandrei.peta2005@gmail.com\n+40733056003")
            }
        }
    }

}
*/
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProHikingTheme {
        ProfileScreen(rememberNavController())
    }
}

private val optionsList: ArrayList<OptionsData> = ArrayList()

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current.applicationContext
) {
    var listPrepared by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()
            prepareOptionsData()
            listPrepared = true
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        if (listPrepared) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {

                    // User's image, name, email
                    UserDetails(context = context)
                }

                items(optionsList) { item ->
                    OptionsItemsStyle(item = item, context = context)

                }
            }
        }
    }


}

@Composable
private fun UserDetails(context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(100.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_outline_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(weight = 3f, fill = false)
                .padding(16.dp)
        ) {
            //User's username
            Text(
                text = "Prohiker",
                style = TextStyle(
                    fontSize = 22.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            //Email
            Text(
                text = "email123@email.com",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Gray,
                    letterSpacing = (0.8).sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            //Location
            Text(
                text = "Bucuresti, RO",
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
private fun OptionsItemsStyle(item: OptionsData, context: Context) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {

            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,


        ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = item.icon,
            contentDescription = item.title,
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
                    text = item.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))

                //Sub-title
                Text(
                    text = item.subTitle,
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
                contentDescription = item.title,
                tint = Color.Black.copy(alpha = 0.70f)
            )

        }
    }
}

private fun prepareOptionsData() {
    val appIcons = Icons.Outlined
    optionsList.add(
        OptionsData(
            icon = appIcons.Person,
            title = "Account",
            subTitle = "Manage your account"
        )
    )
    optionsList.add(
        OptionsData(
            icon = appIcons.Explore,
            title = "Statistics",
            subTitle = "Check out your stats"
        )
    )
    optionsList.add(
        OptionsData(
            icon = appIcons.ContactPage,
            title = "Contact",
            subTitle = "Contact us"
        )
    )
}

data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)
/*
@Preview(showBackground = true, showSystemUi = true)
@ExperimentalMaterialApi
@Composable
fun HomeScreen() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true) {

                    }
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,


                ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = item.icon,
                    contentDescription = item.title,
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
                            text = item.title,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))

                        //Sub-title
                        Text(
                            text = item.subTitle,
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
                        contentDescription = item.title,
                        tint = Color.Black.copy(alpha = 0.70f)
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {

                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        },
                        modifier = Modifier.weight(weight = 1f, fill = false)
                    ) {

                    }

                }
            }
        }
}
*/




