package com.example.whichbin.http

data class LoginResult(
    val code: Int,
    val credits: Int,
    val username: String,
    val uid: Int,
    val status: String
)