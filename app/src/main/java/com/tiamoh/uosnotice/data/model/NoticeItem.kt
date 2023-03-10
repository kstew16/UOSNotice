package com.tiamoh.uosnotice.data.model

import java.util.*

data class NoticeItem(
    val id:String = UUID.randomUUID().toString(),
    val title:String="",
    val writer:String,
    val date:String,
    val url:String
    )