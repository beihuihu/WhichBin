package com.example.whichbin.http

data class SubmitResult(
    val code: Int,
    val credits: Int,
    val name: String,
    val status: String
)