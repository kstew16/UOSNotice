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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.ui.theme.UOSMain

@Composable
fun StartLoginScreen(
    navController: NavHostController,
    onLoginButtonClicked: (String,String) -> Unit
    //,modifier:Modifier = Modifier
) {
    //SecuredSharedPreferences
    var idText by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue())}
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue()) }


    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp,
                end = 30.dp,
                top = 100.dp,
                bottom = 150.dp)
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
                color = UOSMain,
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
                enabled = (idText.text!="" && password.text!=""),
                onClick = { onLoginButtonClicked(idText.text,password.text) },
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .width(width = 300.dp)
                    .height(height = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = UOSMain, contentColor = Color.White)
            ) {
                Text(text = "로그인")
            }

            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }
    }
}