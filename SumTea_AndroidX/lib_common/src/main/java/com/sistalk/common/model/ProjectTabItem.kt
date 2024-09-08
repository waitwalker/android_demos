package com.sistalk.common.model

/*
* 项目item
* */
data class ProjectTabItem(val id: Int, val name: String)

data class ProjectSubList(val datas: MutableList<ProjectSubInfo>)


data class ProjectSubInfo(
    val id: Int?,
    val author: String?,
    val desc: String?,
    val envelopePic: String?,
    val link: String?,
    val niceDate: String?,
    val title: String?,
    val shareUser: String?
)
