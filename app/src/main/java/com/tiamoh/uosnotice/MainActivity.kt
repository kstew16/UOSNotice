package com.tiamoh.uosnotice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.tiamoh.uosnotice.screen.NoticeViewModel
import com.tiamoh.uosnotice.ui.theme.UosNoticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val noticeViewModel:NoticeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val noticeViewModel: NoticeViewModel = hiltViewModel()
        setContent {
            UosNoticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UosNoticeApp(noticeViewModel)
                }
            }
        }
    }
}