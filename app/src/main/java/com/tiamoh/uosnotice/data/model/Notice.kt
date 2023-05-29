package com.tiamoh.uosnotice.data.model

import java.util.*

data class Notice(
    val id:String = UUID.randomUUID().toString(),
    val no:String="",
    val sequence:String="",
    val title:String="",
    val writer:String="",
    val date:String="",
    val url:String=""
    )