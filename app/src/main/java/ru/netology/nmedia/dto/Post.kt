package ru.netology.nmedia.dto


data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likesCount: Int = 755,
    val likedByMe: Boolean = false,
    val shareCount: Int = 700,
    val views: Int = 0,
    val video: PostVideo? = null
)


data class PostVideo(
    val id: Long,
    val name: String = "",
    val url: String = "",
    val views: Int = 2,
)