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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.Routes
import com.tiamoh.uosnotice.ui.theme.UOSLight
import com.tiamoh.uosnotice.ui.theme.UOSMain
import java.util.regex.Pattern

@Composable
fun StartSettingsScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
) {
    var darkMode by remember { mutableStateOf(false) }
    var notificationMode by remember { mutableStateOf(0) }
    var newKeyword by remember { mutableStateOf("") }
    var keywords by remember { mutableStateOf(listOf("등록금", "수강신청")) }
    var notificationEnabled by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var addButtonEnabled by remember { mutableStateOf(false)}
    var selectedOption = remember { mutableStateOf(NotificationOption.All) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Notice.routeName) }) {
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
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "새 공지 알림 받기",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = UOSMain,
                                checkedTrackColor = UOSMain
                            )
                        )
                    }

                    if (notificationEnabled) {
                        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

                        Column(Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "알림을 받을 공지",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            RadioGroup(
                                options = NotificationOption.values().toList(),
                                selectedOption = selectedOption.value,
                                onOptionSelected = {
                                    selectedOption.value = it
                                }
                            )
                        }
                    }
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
                items(keywords) { keyword ->
                    KeywordItem(
                        keyword = keyword,
                        onDeleteClick = { keywords = keywords - keyword }
                    )
                }
                item {
                    AddKeywordItem(onAddClick = {
                        isDialogOpen = true
                    })
                }
            }
        }
        if(isDialogOpen){
            newKeyword = ""
            AlertDialog(
                onDismissRequest = { isDialogOpen = false },
                title = { Text("키워드 추가하기") },
                text = {
                    TextField(
                        value = newKeyword,
                        onValueChange = {
                            newKeyword = it
                            addButtonEnabled = isValidInput(it) },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = UOSMain,
                            unfocusedIndicatorColor = UOSLight,
                            disabledIndicatorColor = UOSLight
                        ),
                        placeholder = {Text("한글, 영문, 숫자 입력 가능")}
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            keywords = keywords + newKeyword
                            isDialogOpen = false
                        },
                        enabled = addButtonEnabled,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = UOSMain,
                            contentColor = Color.White,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text("추가")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { isDialogOpen = false },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = UOSMain,
                            contentColor = Color.White
                        )
                    ) {
                        Text("취소")
                    }
                }
            )
        }
    }
}

fun isValidInput(input:String):Boolean{
    val ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9\\s]+$")
    return ps.matcher(input).matches()
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
            tint = UOSMain,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "키워드 추가하기",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

enum class NotificationOption {
    All,
    KeywordOnly
}

@Composable
fun RadioGroup(
    options: List<NotificationOption>,
    selectedOption: NotificationOption,
    onOptionSelected: (NotificationOption) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable(onClick = { onOptionSelected(option) })
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = null
                )
                Text(
                    text = if(option == NotificationOption.All) "모든 공지" else  "키워드가 포함된 공지",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
