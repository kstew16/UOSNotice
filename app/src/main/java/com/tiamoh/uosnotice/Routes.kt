package com.tiamoh.uosnotice

sealed class Routes(val routeName: String) {
    object Login : Routes("Login")
    object Notice : Routes("Notice")
    object Settings : Routes("Settings")
}