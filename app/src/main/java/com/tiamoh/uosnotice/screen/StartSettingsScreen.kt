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
fun SettingsPage() {
    var darkMode by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(true) }
    var pushNotifications by remember { mutableStateOf(true) }
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
            Text(
                text = "테마 설정",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Switch(
                    checked = darkMode,
                    onCheckedChange = { darkMode = it },
                    modifier = Modifier.weight(1f)
                )
                Text(text = "다크 모드", modifier = Modifier.weight(3f))
            }
            Text(
                text = "알림 설정",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Switch(
                    checked = notificationEnabled,
                    onCheckedChange = { notificationEnabled = it },
                    modifier = Modifier.weight(1f)
                )
                Text(text = "알림 켜기", modifier = Modifier.weight(3f))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){

                    Text(
                        text = "새 공지 알림 켜기",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Checkbox(
                        checked = emailNotifications,
                        onCheckedChange = { emailNotifications = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ){
                    Text(
                        text = "키워드 알림만 켜기",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Checkbox(
                        checked = pushNotifications,
                        onCheckedChange = { pushNotifications = it },
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
                    LanguageItem(
                        language = Keyword,
                        onDeleteClick = { selectedKeyWords = selectedKeyWords - Keyword }
                    )
                }
                item {
                    AddLanguageItem(onAddClick = { selectedKeyWords = selectedKeyWords + " 키워드 추가하기" })
                }
            }
        }
    }
}


@Composable
fun LanguageItem(
    language: String,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = language, modifier = Modifier.weight(1f))
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun AddLanguageItem(
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



@Preview
@Composable
fun SettingsPreview(){
    SettingsPage()
}
