package ru.netology.nmedia.dto



data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int,
    var shareCount: Int,
    var visibilityCount: Int,
    var likedByMe: Boolean = false
)
