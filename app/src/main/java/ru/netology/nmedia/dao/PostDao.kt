package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.entity.PostAndVideo
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostVideoEntity


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAllPosts(): LiveData<List<PostEntity>>

    fun getAll(): LiveData<List<PostAndVideo>> {

        return getAllPosts().map { list ->
            list.map { post ->
                val video = post.videoId?.let { getVideoEntity(post.videoId) }
                PostAndVideo(video?.value?.get(0), post)
            }
        }
    }

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAllWithQuery(): LiveData<List<PostAndVideo>>

    @Query("SELECT * FROM PostVideoEntity WHERE id = :id")
    fun getVideoEntity(id: Long): LiveData<List<PostVideoEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Insert
    fun insert(postVideo: PostVideoEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Query("UPDATE PostVideoEntity SET name = :name, url = :url, views = :views WHERE id = :id")
    fun updateContentById(id: Long, name: String, url: String, views: Int)
    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    fun save(postVideo: PostVideoEntity) =
        if (postVideo.id == 0L) insert(postVideo)
        else updateContentById(postVideo.id, postVideo.name, postVideo.url, postVideo.views)

    @Query(
        """
           UPDATE PostEntity SET
               likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id"""
    )
    fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query(
        """
           UPDATE PostEntity SET
               shareCount = shareCount + 1
           WHERE id = :id;
        """
    )
    fun shareById(id: Long)
}
