package com.tiamoh.uosnotice.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tiamoh.uosnotice.ui.theme.UOSMain

@Composable
fun StartSettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(true) }
    var keywordNotificationEnabled by remember { mutableStateOf(true) }
    var selectedKeyWords by remember { mutableStateOf(listOf("등록금", "수강신청")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null,tint=Color.White)
                    }
                },
                backgroundColor = UOSMain,
                contentColor = Color.White
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
                    //Todo 라디오버튼으로 업데이트 또는 큰 거 알림 하나에 라디오 둘
                    Text(
                        text = "새 공지 알림 켜기",
                    )
                    Checkbox(
                        checked = notificationEnabled,
                        onCheckedChange = { notificationEnabled = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
                    Text(
                        text = "키워드 알림만 켜기",
                    )
                    Checkbox(
                        checked = keywordNotificationEnabled,
                        onCheckedChange = { keywordNotificationEnabled = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            Text(
                text = "공지 알림 키워드 추가",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(selectedKeyWords) { Keyword ->
                    KeywordItem(
                        keyword = Keyword,
                        onDeleteClick = { selectedKeyWords = selectedKeyWords - Keyword }
                    )
                }
                item {

                    // Todo 다이얼로그 켜서 텍스트 입력받기
                    AddKeywordItem(onAddClick = { selectedKeyWords = selectedKeyWords + " 키워드 추가하기" })
                }
            }
        }
    }
}


@Composable
fun KeywordItem(
    keyword: String,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = keyword, modifier = Modifier.weight(1f))
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun AddKeywordItem(
    onAddClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onAddClick)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "키워드 추가하기",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SettingsPreview(){
    StartSettingsScreen()
}
