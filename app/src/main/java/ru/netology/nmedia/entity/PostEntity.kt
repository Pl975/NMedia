package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostVideo


@Entity
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likesCount: Int = 755,
    val likedByMe: Boolean = false,
    val shareCount: Int = 700,
    val views: Int = 0,

    @ColumnInfo(name = "video_id")
    val videoId: Long? = null

) {
    fun toDto() = run {
        Post(
            id, author, content,
            published, likesCount, likedByMe,
            shareCount, views, null
        )
    }

    companion object {
        fun postFromDto(dto: Post) =
            with(dto) {
                PostEntity(
                    id, author, content,
                    published, likesCount, likedByMe,
                    shareCount, views, video?.id
                )
            }
    }
}

@Entity
class PostVideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String = "",
    val url: String = "",
    val views: Int = 2
)

class PostAndVideo(
    @Embedded
    val video: PostVideoEntity?,
    @Relation(entity = PostEntity::class, parentColumn = "id", entityColumn = "video_id")
    val post: PostEntity
) {
    fun toDto() = with(post) {
        Post(
            id, author, content,
            published, likesCount, likedByMe,
            shareCount, views,
            video?.run {
                PostVideo(id, name, url, views)
            }
        )
    }

    companion object {
        fun postFromDto(dto: Post) =
            PostEntity(
                dto.id, dto.author, dto.content,
                dto.published, dto.likesCount, dto.likedByMe,
                dto.shareCount, dto.views, dto.video?.id
            )

        fun videoFromDto(dto: PostVideo) =
            PostVideoEntity(
                dto.id, dto.name, dto.url, dto.views
            )
    }
}