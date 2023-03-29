package com.tiamoh.uosnotice

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tiamoh.uosnotice.screen.NoticeViewModel
import com.tiamoh.uosnotice.screen.StartLoginScreen
import com.tiamoh.uosnotice.screen.StartNoticeScreen
import com.tiamoh.uosnotice.screen.StartSettingsScreen
import com.tiamoh.uosnotice.util.EncryptedAccountManager
import kotlinx.coroutines.*
import org.jsoup.Connection
import org.jsoup.Jsoup


enum class UOSNoticeScreens(){
    Login,
    Notice,
    Settings
}

@Composable
fun UosNoticeApp(noticeViewModel: NoticeViewModel) {
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
            StartLoginScreen(navController = navController,noticeViewModel,encryptedAccountManager) { id, passWord ->
                // retrofit2 를 이용하여 서울시립대 포털사이트에 로그인
                // 실패시 Notice 띄우고 성공시 NoticePage 로 연결해야함
                //println(id.plus(passWord))
                // Todo: 로그 지워라
                Log.d("d",id)
                Log.d("d",passWord)
                //portalLogin(id = id, pw = passWord)
                //portalLogin(id = "kstew16", pw = "projectPassword16!")
                //noticeViewModel.loginAndCrawl("kstew16","projectPassword16!")
                noticeViewModel.loginAndCrawl(id,passWord)
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
                noticeViewModel = noticeViewModel
            )
        }

        composable(Routes.Settings.routeName){
            //Log.d("d","Settings Clicked")
            StartSettingsScreen(navController)
        }
    }
}

fun portalLogin(id:String,pw:String){
    CoroutineScope(Dispatchers.IO).launch{
        val loginUrl = "https://portal.uos.ac.kr/user/login.face"
        val loginProcessUrl = "https://portal.uos.ac.kr/user/loginProcess.face"
        val homeUrl = "https://portal.uos.ac.kr/portal/default/test/tesr_stu_default.page"

        val loginPageResponse: Connection.Response = Jsoup.connect("https://portal.uos.ac.kr/user/login.face")
            .timeout(3000)
            .header(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
            )
            .header("Upgrade-Insecure-Requests","1")
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36")
            .header("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"")
            .header("sec-ch-ua-mobile","?1")
            .header("sec-ch-ua-platform","\"Android\"")
            .method(Connection.Method.GET)
            .execute()
        val cookies = loginPageResponse.cookies()
        val response = Jsoup.connect(loginProcessUrl)
            .data("ssoId",id,"password",pw)
            .cookies(cookies)
            .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36")
            .timeout(3000)
            .header("Upgrade-Insecure-Requests","1")
            .header("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"")
            .header("sec-ch-ua-mobile","?1")
            .header("sec-ch-ua-platform","\"Android\"")
            .method(Connection.Method.POST)
            .execute()
        val loginCookie = response.cookies()
        if(!loginCookie.containsKey("EnviewSessionID")){
            throw Exception("Login failed")
        }
        val homePage = Jsoup.connect(homeUrl).cookies(loginCookie).get()



    }
}