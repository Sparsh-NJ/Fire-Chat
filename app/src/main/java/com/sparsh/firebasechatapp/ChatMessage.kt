package com.sparsh.firebasechatapp

class ChatMessage(
    val text :String,
    val id :String,
    val fromId :String,
    val toId :String,
    val timeStamp:Long
){
    constructor():this("","","","",-1)
}