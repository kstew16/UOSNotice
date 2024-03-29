package com.tiamoh.uosnotice.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.tiamoh.uosnotice.R
import com.tiamoh.uosnotice.ui.theme.UOSMain
import kotlinx.coroutines.launch
import java.util.regex.Pattern

//Todo : 키보드 밖 터치시 포커스 잃게
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun StartLoginScreen(
    navController: NavHostController,
    onLoginButtonClicked: (String,String) -> Unit
    //,modifier:Modifier = Modifier
) {
    //SecuredSharedPreferences
    var idText by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue())}
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource()}
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManger?.clearFocus()
            }){
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    top = 100.dp,
                    bottom = 150.dp
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.uos_mark),
                contentDescription = "() 1",
                modifier = Modifier
                    .width(width = 153.dp)
                    .height(height = 93.dp))

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

            TextField(
                modifier = Modifier
                    .width(300.dp)
                ,

                value = idText,
                onValueChange = {if(isValidID(it.text) || it.text=="") idText = it
                },
                keyboardActions = KeyboardActions(onDone = {
                    //keyboardController?.hide()
                    focusManger?.moveFocus(FocusDirection.Down)
                }),
                label = { Text("아이디") },
                singleLine = true
            )
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))

            TextField(
                modifier = Modifier
                    .width(300.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,
                value = password,
                onValueChange = {
                    password = it
                },
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManger?.clearFocus()
                }),
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))

            Button(
                enabled = (idText.text!="" && password.text!=""),
                onClick = { onLoginButtonClicked(idText.text,password.text) },
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .width(width = 300.dp)
                    .height(height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = UOSMain,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = "로그인")
            }

            Spacer(
                modifier = Modifier
                    .height(height = 30.dp))
        }
    }
}

fun isValidID(input:String):Boolean{
    val ps = Pattern.compile("^[a-zA-Z\\d]+$")
    return ps.matcher(input).matches()
}
/*
fun isValidPW(input:String):Boolean{
    val ps = Pattern.compile("^[A-Za-z\\d\$@\$!%*#?&]+$")
    return ps.matcher(input).matches()
}

 */

