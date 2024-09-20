package ru.netology.nmedia.dto



data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int,
    val shareCount: Int,
    val visibilityCount: Int,
    val likedByMe: Boolean = false
)
