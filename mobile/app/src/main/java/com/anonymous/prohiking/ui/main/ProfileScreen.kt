package com.anonymous.prohiking.ui.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.R
import com.anonymous.prohiking.ui.theme.ProHikingTheme

@Composable
fun ProfileScreen(navController: NavController) {
    //val preferencesRepository = ProHikingApplication.instance.preferencesRepository
   // val username = preferencesRepository.username.collectAsState(initial = "")
   // ${username.value?:"ProHiker"
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.lovepik_com_400203927_green_forest),
            contentDescription = "Register",
            modifier = Modifier
                .fillMaxSize()
                .blur(0.dp),
            contentScale = ContentScale.Crop
        )

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
            Spacer( modifier = Modifier.size(20.dp))
            Text("Email:", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(modifier = Modifier.size(20.dp))

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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {

    ProHikingTheme {
        ProfileScreen(rememberNavController())
    }
}


