package com.tiamoh.uosnotice.screen


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.ui.theme.SemiGrayScreen


@Composable
fun StartNoticeScreen(
    navController: NavHostController,
    onMenuClicked: (Int)->Unit,
    onSettingsClicked:()->Unit
    //,modifier:Modifier = Modifier
){
    var backKeyPressedTime = remember { mutableStateOf(0L) }
    lateinit var toast:Toast
    val context = LocalContext.current
    val viewModel:LoginScreenViewModel
    var isTitleExpanded by remember { mutableStateOf(false) }

    val sampleMenuArr = arrayOf(
        "키워드 모아보기",
        "홈페이지 공지",
        "학과 공지",
        "장학 공지",
        "후생 복지",
        "취업정보",
        "비교과 프로그램",
        "취업,진로 프로그램"
    )
    //var noticeName by viewModel.screenTitle.observeAsState(initial = asdf)

    var noticeName = remember {
        sampleMenuArr[0]
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    DropdownMenu(
                        expanded = isTitleExpanded,
                        modifier = Modifier.wrapContentSize(),
                        onDismissRequest = { isTitleExpanded = false }) {
                    for(i in sampleMenuArr.indices){
                        DropdownMenuItem(onClick = {
                            onMenuClicked(i)
                            noticeName = sampleMenuArr[i]
                            isTitleExpanded = false
                        }) {
                            Text(sampleMenuArr[i])
                        }
                    }

                    }
                        Button(onClick = { isTitleExpanded=true },
                            Modifier.wrapContentWidth(),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = (-4).dp
                            )
                        ){
                            Text(
                                text = noticeName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(Icons.Filled.ArrowDropDown,contentDescription = null, modifier = Modifier.size(24.dp))
                        }
                    Spacer(modifier = Modifier.fillMaxWidth())
                },
                elevation =  0.dp,
                actions = {
                    IconButton(onClick = { onSettingsClicked() }) {
                        Icon(Icons.Filled.Settings,contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
            //TopBarWithTitle(title = noticeName,
            //    onMenuClicked = {onMenuClicked()},
            //onSettingsClicked={onSettingsClicked()})
        }
    ) {
        BackHandler() {
            if(System.currentTimeMillis()>backKeyPressedTime.value+2000){
                backKeyPressedTime.value = System.currentTimeMillis()
                toast = Toast.makeText(context,"버튼을 한 번 더 눌러 앱을 종료하세요",Toast.LENGTH_LONG)
                toast.show()
            }else{
                toast.cancel()
                    val context = LocalContext as Context
                context.findActivity().finish()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(){
            
        }
        LazyColumn(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(SemiGrayScreen)
        ){

            item{
                Button(onClick = {
                    // 뷰모델 처리하는게 낫지 않나
                    noticeName = "바뀜?"
                }) {

                }
            }
        }
    }


}


fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}
@Preview
@Composable
fun TopBarWithTitle(
    //title:String,
    //onMenuClicked: () -> Unit,
    //onSettingsClicked: () -> Unit
) {
    TopAppBar(elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "title",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier,
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Filled.ArrowDropDown,contentDescription = null)
            }
            Row(

                verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Settings,contentDescription = null)
                }
            }
        }

    }
}