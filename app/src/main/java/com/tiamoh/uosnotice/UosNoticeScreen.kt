package com.tiamoh.uosnotice

import android.app.Dialog
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tiamoh.uosnotice.data.model.NoticeViewModel
import com.tiamoh.uosnotice.screen.StartLoginScreen
import com.tiamoh.uosnotice.screen.StartNoticeScreen
import com.tiamoh.uosnotice.screen.StartSettingsScreen


enum class UOSNoticeScreens(){
    Login,
    Notice,
    Settings
}

@Composable
fun UosNoticeApp(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val defaultNoticeType = 0
    val noticeViewModel = NoticeViewModel(defaultNoticeType)
    NavHost(
        navController = navController, startDestination = Routes.Login.routeName
    ) {
        composable(Routes.Login.routeName) {
            StartLoginScreen(navController = navController) { id, passWord ->
                // retrofit2 를 이용하여 서울시립대 포털사이트에 로그인
                // 실패시 Notice 띄우고 성공시 NoticePage 로 연결해야함
                //println(id.plus(passWord))
                Log.d("d",id)
                Log.d("d",passWord)
                navController.navigate(Routes.Notice.routeName)
            }
        }

        composable(Routes.Notice.routeName){
            StartNoticeScreen(navController = navController,
                onMenuClicked = { selectedIndex ->

                                val sampleMenuArr = arrayOf(
                                    "키워드 모아보기",
                                    "홈페이지 공지",
                                    "학과 공지",
                                    "장학 공지",
                                    "후생 복지",
                                    "[UOStory] 취업정보",
                                    "[UOStory] 비교과 프로그램",
                                    "[UOStory] 취업,진로 프로그램"
                                )
                    Toast.makeText(context, sampleMenuArr[selectedIndex], Toast.LENGTH_SHORT).show()
                },
                onSettingsClicked = {navController.navigate(Routes.Settings.routeName)},
                defaultNoticeType = defaultNoticeType,
                noticeViewModel = noticeViewModel
            )
        }

        composable(Routes.Settings.routeName){
            //Log.d("d","Settings Clicked")
            StartSettingsScreen(navController)
        }
    }
}