package com.tiamoh.uosnotice

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tiamoh.uosnotice.screen.*
import com.tiamoh.uosnotice.util.EncryptedAccountManager


enum class UOSNoticeScreens(){
    Login,
    Notice,
    Settings
}

@Composable
fun UosNoticeApp(
    noticeViewModel: NoticeViewModel,
    sessionViewModel: SessionViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val defaultNoticeType = 0
    val encryptedAccountManager = EncryptedAccountManager(context)
    //val noticeViewModel = NoticeViewModel()
    //val noticeViewModel:NoticeViewModel = viewModel()
    //val noticeViewModel:NoticeViewModel = hiltViewModel()
    NavHost(
        navController = navController, startDestination = Routes.Login.routeName
    ) {
        composable(Routes.Login.routeName) {
            StartLoginScreen(navController = navController,noticeViewModel,encryptedAccountManager,sessionViewModel) { id, passWord ->
                // retrofit2 를 이용하여 서울시립대 포털사이트에 로그인
                // 실패시 Notice 띄우고 성공시 NoticePage 로 연결해야함
                sessionViewModel.putAccountInfo(id,passWord)
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
                                    "[UOStory] 취업정보",
                                    "[UOStory] 비교과 프로그램",
                                    "[UOStory] 취업,진로 프로그램"
                                )
                    Toast.makeText(context, sampleMenuArr[selectedIndex], Toast.LENGTH_SHORT).show()
                },
                onSettingsClicked = {navController.navigate(Routes.Settings.routeName)},
                defaultNoticeType = defaultNoticeType,
                noticeViewModel = noticeViewModel,
                sessionViewModel = sessionViewModel
            )
        }

        composable(Routes.Settings.routeName){
            // Settings Clicked"
            StartSettingsScreen(navController,sessionViewModel)
        }
    }
}