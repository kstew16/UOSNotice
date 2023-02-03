package com.tiamoh.uosnotice

import android.app.Dialog
import android.util.Log
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tiamoh.uosnotice.screen.OpenNoticesDialog
import com.tiamoh.uosnotice.screen.StartLoginScreen
import com.tiamoh.uosnotice.screen.StartNoticeScreen


enum class UOSNoticeScreens(){
    Login,
    Notice,
    Settings
}

@Composable
fun UosNoticeApp(){
    val navController = rememberNavController()
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
                onMenuClicked = {navController.navigate(Routes.NoticesDialog.routeName)},
                onSettingsClicked = {navController.navigate(Routes.Settings.routeName)}
            )
        }

        composable(Routes.Settings.routeName){
            Log.d("d","Settings Clicked")
            //StartSettingsScreen()
        }

        composable(Routes.NoticesDialog.routeName){
            Log.d("d","Dialog Clicked")
            OpenNoticesDialog(true){

            }
        }
    }
}
fun dummyFunction(){
    Log.d("d","dummy")
}