package com.tiamoh.uosnotice.screen


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tiamoh.uosnotice.R
import com.tiamoh.uosnotice.data.model.NoticeItem
import com.tiamoh.uosnotice.ui.theme.SemiGrayScreen
import com.tiamoh.uosnotice.ui.theme.UOSMain


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

    var searchText = remember { mutableStateOf(TextFieldValue("")) }
    val noticeScrollState = rememberScrollState()
    var noticeName = remember {
        sampleMenuArr[0]
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    DropdownMenu(
                        expanded = isTitleExpanded,
                        modifier = Modifier
                            .wrapContentSize()
                            .background(Color.Red),
                        onDismissRequest = { isTitleExpanded = false }) {
                        for(i in sampleMenuArr.indices){
                            DropdownMenuItem(onClick = {
                                onMenuClicked(i)
                                noticeName = sampleMenuArr[i]
                                isTitleExpanded = false
                            },
                            ) {
                                Text(sampleMenuArr[i])
                            }
                        }
                    }
                        Button(onClick = { isTitleExpanded=true },
                            Modifier
                                .wrapContentWidth()
                                .background(Color.Transparent)
                            ,
                            colors = ButtonDefaults.buttonColors(
                                Color.Transparent
                            ),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = (-4).dp
                            )
                        ){
                            Text(
                                text = noticeName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.background(Color.Transparent),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    Spacer(modifier = Modifier.fillMaxWidth())
                },
                elevation =  0.dp,
                actions = {
                    IconButton(onClick = { onSettingsClicked() }) {
                        Icon(Icons.Filled.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White)
                    }
                },
                backgroundColor = UOSMain
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
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
            ,
            modifier = Modifier
                .fillMaxSize()
                .background(SemiGrayScreen)

        ) {
            SearchView(state = searchText)
            NoticeList(navController = navController, state = searchText){
                toast = Toast.makeText(context,"$it 로 리디렉션됩니다.",Toast.LENGTH_LONG)
                toast.show()
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

// https://johncodeos.com/how-to-add-search-in-list-with-jetpack-compose/
@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp)
        ,
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(30.dp), // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Gray,
            leadingIconColor = Color.Gray,
            trailingIconColor = Color.Gray,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),

    )
}


@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(textState)
}

@Preview(showBackground = true)
@Composable
fun NoticeScreenPreview(){
    val navController = rememberNavController()
    val onMenuClicked = {_:Int-> }
    val onSettingsClicked = {}
    StartNoticeScreen(navController=navController,onMenuClicked = onMenuClicked,onSettingsClicked=onSettingsClicked)
}

@Composable
fun NoticeList(navController: NavController,state: MutableState<TextFieldValue>, onItemClick: (String) -> Unit){
    val notices = getListOfNotices()
    var filteredNotices:ArrayList<NoticeItem>
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(10.dp)
    ){
        var searchedText = state.value.text
        filteredNotices = if(searchedText.isEmpty()){notices}
        else{
            val resultList = ArrayList<NoticeItem>()
            for(notice in notices){
                if(notice.title.contains(searchedText)) resultList.add(notice)
            }
            resultList
        }
        items(filteredNotices){ filteredNotice->
            NoticeListItem(noticeItem = filteredNotice, onItemClick = {onItemClick(filteredNotice.url)})

        }
    }
}

fun getListOfNotices(): ArrayList<NoticeItem>{
    val sampleList = ArrayList<NoticeItem>()
    for(i in 1 until 10){
        sampleList.add(
            NoticeItem(
                title = "$i 번째 공지를 표시하는 중입니다.",
                writer = if(i%3==0) "은붕" else if(i%3==1)"가붕" else "윤똥",
                date = if(i%3==0) "1999-01-06" else if(i%3==1) "2000-09-14" else "2002-06-08",
                url = "www.google.com"
            )
        )
    }
    return sampleList
}

@Composable
fun NoticeListItem(noticeItem: NoticeItem,onItemClick: (String) -> Unit) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(15.dp))
        .clickable(onClick = { onItemClick(noticeItem.url) })
        .background(Color.White)
        .height(150.dp)
        .fillMaxWidth()
        .padding(PaddingValues(8.dp, 16.dp))

    ){
        Row(
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = noticeItem.title, fontSize = 23.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = noticeItem.writer, fontSize = 20.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    Text(text = noticeItem.date, fontSize = 20.sp, color = Color.LightGray)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoticeListItemPreview() {
    val sampleItem = NoticeItem(
        "서울시립대학교 국제도시과학대학원 강사 채용을 하는 걸 존나 길게 늘려 써서 알아서 자르게",
        "국제과학도시대학원",
        "2023-01-31",
        ""
    )
    NoticeListItem(
        sampleItem,
        onItemClick = {_->}
    )
}