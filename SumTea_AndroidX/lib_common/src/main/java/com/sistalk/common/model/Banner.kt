package com.sistalk.common.model

data class Banner(
    val it: Int? = 0,
    val url: String? = "",
    val imagePath: String? = "",
    val title: String? = "",
    val desc: String? = "",
    val isVisible: Int? = 0,
    val order: Int? = 0,
    val type: Int? = 0,
)
