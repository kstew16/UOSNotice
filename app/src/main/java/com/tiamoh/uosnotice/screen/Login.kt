package com.tiamoh.uosnotice.screen

import com.tiamoh.uosnotice.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginPage() {
    var idText by rememberSaveable{ mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("") }


    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 28.dp,
                end = 28.dp,
                top = 150.dp,
                bottom = 300.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.uos_mark),
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
                    .width(width = 300.dp)
                    .height(height = 50.dp))
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }

        item {
            TextField(
                modifier = Modifier.width(300.dp),
                value = idText,
                onValueChange = { idText = it },
                label = { Text("아이디") },
            )
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }

        item {
            TextField(
                modifier = Modifier.width(300.dp),
                value = password,
                onValueChange = {
                    password = it
                    //if(idText!="") ButtonDefaults.buttonColors(Color(0xff005ead))
                },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }


        item {

            Button(
                onClick = { },
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .width(width = 300.dp)
                    .height(height = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff005ead), contentColor = Color.White)
            ) {
                Text(text = "로그인")
            }

            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }
    }
}