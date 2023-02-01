package com.tiamoh.uosnotice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiamoh.uosnotice.ui.theme.UosNoticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UosNoticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UosNoticeTheme {
        Greeting("Android")
    }
}

@Composable
fun Login() {
    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 28.dp,
                end = 29.dp,
                top = 153.dp,
                bottom = 313.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "() 1",
                modifier = Modifier
                    .width(width = 153.dp)
                    .height(height = 93.dp))
        }

        item {
            Text(
                text = "서울시립대학교 공지사항",
                color = Color(0xff005ead),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(width = 303.dp)
                    .height(height = 54.dp))
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }

        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .width(width = 168.dp)
                    .height(height = 28.dp)
                    .padding(start = 96.dp,
                        top = 330.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(width = 168.dp)
                        .height(height = 28.dp)
                        .background(color = androidx.compose.ui.graphics.Color.White))
                Text(
                    text = "아이디",
                    color = Color(0xffebebeb),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light))
            }
            Spacer(
                modifier = Modifier
                    .height(height = 29.dp))
        }

        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .width(width = 168.dp)
                    .height(height = 28.dp)
                    .padding(start = 96.dp,
                        top = 387.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(width = 168.dp)
                        .height(height = 28.dp)
                        .background(color = androidx.compose.ui.graphics.Color.White))
                Text(
                    text = "비밀번호",
                    color = Color(0xffebebeb),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp))
            }
            Spacer(
                modifier = Modifier
                    .height(height = 29.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .width(width = 264.dp)
                    .height(height = 43.dp)
                    .background(color = Color(0xff005ead)))
        }

        item {
            Text(
                text = "로그인",
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(width = 80.dp)
                    .height(height = 15.dp))
        }

    }
}