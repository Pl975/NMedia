package ru.netology.nmedia.dto



data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likesCount: Int = 300,
    val shareCount: Int = 700,
    val visibilityCount: Int = 550_000,
    val likedByMe: Boolean,
    val sharedByMe: Boolean
)
