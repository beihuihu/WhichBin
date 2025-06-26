package com.example.whichbin.http

data class RankingResult(
    val code: Int,
    val ranking: List<RankingItem>,
    val status: String
)