package com.tiamoh.uosnotice.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.R
import com.tiamoh.uosnotice.Routes
import com.tiamoh.uosnotice.data.api.dto.AccountDTO
import com.tiamoh.uosnotice.ui.theme.UOSMain
import com.tiamoh.uosnotice.util.EncryptedAccountManager
import com.tiamoh.uosnotice.util.SharedPreferenceManager
import com.tiamoh.uosnotice.util.findActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

//Todo : 키보드 밖 터치시 포커스 잃게
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun StartLoginScreen(
    navController: NavHostController,
    noticeViewModel: NoticeViewModel,
    encryptedAccountManager: EncryptedAccountManager,
    sessionViewModel: SessionViewModel,
    onLoginButtonClicked: (String, String) -> Job
    //,modifier:Modifier = Modifier
) {
    //SecuredSharedPreferences

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource()}
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false)    }
    val isLoggedIn by sessionViewModel.isPortalOnline.observeAsState()
    val sharedPreferenceManager = SharedPreferenceManager((LocalContext.current).findActivity())
    var setRememberAccount by remember { mutableStateOf(sharedPreferenceManager.getSetting("savingId",false))}
    val savedAccount = encryptedAccountManager.getAccount()
    var idText by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue(
        savedAccount.id
    ))}
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver){ mutableStateOf(TextFieldValue(
        savedAccount.pw
    )) }

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
                    bottom = 100.dp
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
                onValueChange =  {

                    if(isValidPW(it.text) || it.text=="")password = it
                    else Log.d("login","Invalid passwordInput ${it.text}")
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
                onClick = { onLoginButtonClicked(idText.text,password.text)
                    isLoading = true
                    if(setRememberAccount){
                        Log.d("loginScreen","saving account")
                        encryptedAccountManager.saveAccount(AccountDTO(idText.text, password.text))
                        sharedPreferenceManager.setSetting("savingId",true)
                    }else{
                        encryptedAccountManager.saveAccount(AccountDTO("", ""))
                        sharedPreferenceManager.setSetting("savingId",false)
                    }
                },
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
                    .height(height = 20.dp))
            RememberAccountCheckbox(initialChecked = setRememberAccount, onCheckedChange = {isChecked->
                setRememberAccount = isChecked
                Log.d("loginScreen","checkbox enabled : $isChecked")
            })

            if(isLoading){
                if(isLoggedIn==true){
                    navController.navigate(Routes.Notice.routeName)
                    isLoading = false
                    Log.d("loginScreen","navigate to Notice")
                }
            }
        }
    }
}

fun isValidID(input:String):Boolean{
    val ps = Pattern.compile("^[a-zA-Z\\d]+$")
    return ps.matcher(input).matches()
}

fun isValidPW(input:String):Boolean{
    val ps = Pattern.compile("^[(~!@\$^*_{}\\[\\]\\\\:,.?)\\w]+$")
    return ps.matcher(input).matches()
}

@Composable
fun RememberAccountCheckbox(onCheckedChange:(Boolean)->Unit,initialChecked:Boolean)
{
    val checkedState = remember { mutableStateOf(initialChecked) }
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
            ){
        Checkbox(
            checked = checkedState.value,
            modifier = Modifier
                .padding(start = 20.dp)
            ,
            onCheckedChange = {
                checkedState.value = it
                onCheckedChange(it) },
        )
        Text(
            text = "계정 정보 기억하기",
            modifier = Modifier.padding(0.dp),
            color = Color.Black,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 16.sp,
            )
        )
    }
}


@Preview
@Composable
fun DefaultPreview(){
    RememberAccountCheckbox(onCheckedChange = {}, initialChecked = false)
}

