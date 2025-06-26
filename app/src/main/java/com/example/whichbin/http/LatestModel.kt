package com.example.whichbin.http

data class LatestModel (
    val bin_url: String,
    val sys: String,
    val code: Int,
    val param_url: String,
    val status: String,
    val version: Int
    )
