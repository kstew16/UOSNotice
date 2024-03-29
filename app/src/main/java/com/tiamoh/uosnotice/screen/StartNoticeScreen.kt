package com.tiamoh.uosnotice.screen


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.data.model.NoticeItem
import com.tiamoh.uosnotice.data.model.NoticeViewModel
import com.tiamoh.uosnotice.ui.theme.SemiGrayScreen
import com.tiamoh.uosnotice.ui.theme.UOSMain


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StartNoticeScreen(
    navController: NavHostController,
    onMenuClicked: (Int)->Unit,
    onSettingsClicked:()->Unit,
    defaultNoticeType:Int,
    noticeViewModel:NoticeViewModel
    //,modifier:Modifier = Modifier
){
    lateinit var toast:Toast
    val context = LocalContext.current
    val noticeList = noticeViewModel.list.observeAsState().value
    val interactionSource = remember { MutableInteractionSource() }
    val focusManger = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ){
         focusManger?.clearFocus()
        },
        topBar = {
            DropBoxTopBar(
                defaultNoticeType = defaultNoticeType,
                onMenuClicked = {noticeViewModel.selectNoticeType(it)},
                onSettingsClicked=onSettingsClicked)
        }
    ) {
        SearchListView(
            onSearchText = {noticeViewModel.filterNoticeBy(it)},
            noticeList = noticeList,
            onItemClick = {
                // Todo : NavHost 이용해서 WebView 띄우기
                toast = Toast.makeText(context,"$it (으)로 리디렉션됩니다.",Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }
    backHandler(toast = Toast.makeText(context,"버튼을 한 번 더 눌러 앱을 종료하세요",Toast.LENGTH_LONG))
}

@Composable
fun backHandler(toast: Toast){
    var backKeyPressedTime by remember { mutableStateOf(0L) }
    BackHandler() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis()
            toast.show()
        }else{
            toast.cancel()
            val context = LocalContext as Context
            context.findActivity().finish()
        }
    }
}

@Composable
fun SearchListView(onSearchText:(String)->Unit, noticeList: List<NoticeItem>?, onItemClick: (String) -> Unit){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
        ,
        modifier = Modifier
            .fillMaxSize()
            .background(SemiGrayScreen)

    ) {
        SearchView(onSearchText = onSearchText)
        NoticeListItem(noticeList = noticeList,onItemClick = onItemClick)
    }
}

@Composable
fun DropBoxTopBar(
    defaultNoticeType:Int,
    onMenuClicked: (Int) -> Unit,
    onSettingsClicked: () -> Unit
){
    val selectedNoticeType = remember{ mutableStateOf(defaultNoticeType) }
    var isTitleExpanded by remember { mutableStateOf(false) }
    // Todo : 이거 data.allNotices 로 관리
    val noticeTypeName = arrayOf(
        "키워드 모아보기",
        "홈페이지 공지",
        "학과 공지",
        "장학 공지",
        "후생 복지",
        "취업정보",
        "비교과 프로그램",
        "취업,진로 프로그램"
    )
    var noticeName by remember {
        mutableStateOf(noticeTypeName[selectedNoticeType.value])
    }

    TopAppBar(
        title = {

            Box(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.CenterStart)){
                Row(){
                    Text(noticeName,
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable(onClick = { isTitleExpanded = true })
                            .background(Color.Transparent),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    DropdownMenu(
                        expanded = isTitleExpanded,
                        modifier = Modifier
                            .wrapContentSize(),
                        onDismissRequest = { isTitleExpanded = false }) {
                        for(i in noticeTypeName.indices){
                            DropdownMenuItem(onClick = {
                                isTitleExpanded = false
                                selectedNoticeType.value = i
                                noticeName = noticeTypeName[i]
                                onMenuClicked(i)
                                Log.d("DropDown","noticeType ${selectedNoticeType.value}")
                            },
                            ) {
                                Text(noticeTypeName[i])
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
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
        backgroundColor = UOSMain,
        modifier = Modifier.height(70.dp)
    )
}

@Composable
fun NoticeListItem(noticeList:List<NoticeItem>?, onItemClick: (String) -> Unit){
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(10.dp)
    ){
        if(!noticeList.isNullOrEmpty()){
            items(noticeList,
                key={
                        notice -> notice.id
                }
            ){ notice->
                NoticeListItem(noticeItem = notice){
                    onItemClick(notice.url)
                }
            }
        }
    }
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

@Composable
fun SearchView(onSearchText:(String)->Unit){
    var searchText: String by rememberSaveable{ mutableStateOf("") }
    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            Log.d("SearchView","call viewModel")
            onSearchText(it)
        },
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp)
        ,
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        singleLine = true,
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
            if (searchText != "") {
                IconButton(
                    onClick = {
                        searchText = "" // Remove text from TextField when you press the 'X' icon
                        Log.d("TopBar","call viewModel")
                        onSearchText("")
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
        )
    )
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}