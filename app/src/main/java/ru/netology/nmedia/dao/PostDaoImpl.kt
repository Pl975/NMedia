package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getLongOrNull
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostVideo
import ru.netology.nmedia.util.NMediaUtils

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL_POST = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_POST_VIDEO_ID} INTEGER,
            FOREIGN KEY (${PostColumns.COLUMN_POST_VIDEO_ID}) 
            REFERENCES ${PostVideoColumns.TABLE}(${PostVideoColumns.COLUMN_ID})
        );
        """.trimIndent()
        val DDL_POST_VIDEO = """
            CREATE TABLE ${PostVideoColumns.TABLE} (
            ${PostVideoColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostVideoColumns.COLUMN_NAME} TEXT NOT NULL,
            ${PostVideoColumns.COLUMN_URL} TEXT NOT NULL,
            ${PostVideoColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_SHARED_BY_ME = "sharedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARES = "shares"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_POST_VIDEO_ID = "postVideoId"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_VIEWS,
            COLUMN_POST_VIDEO_ID
        )
    }

    object PostVideoColumns {
        const val TABLE = "postVideo"
        const val COLUMN_ID = "postVideoId"
        const val COLUMN_NAME = "postVideoName"
        const val COLUMN_URL = "postVideoUrl"
        const val COLUMN_VIEWS = "postVideoViews"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_URL,
            COLUMN_VIEWS
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, NMediaUtils.AUTHOR)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, NMediaUtils.PUBLISHED)
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               shares = shares + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun getVideo(id: Long): PostVideo {
        db.query(
            PostVideoColumns.TABLE,
            PostVideoColumns.ALL_COLUMNS,
            "${PostVideoColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return mapVideo(it)
        }
    }

    private fun mapVideo(cursor: Cursor): PostVideo {
        with(cursor) {
            return PostVideo(
                name = getString(getColumnIndexOrThrow(PostVideoColumns.COLUMN_NAME)),
                url = getString(getColumnIndexOrThrow(PostVideoColumns.COLUMN_URL)),
                views = getInt(getColumnIndexOrThrow(PostVideoColumns.COLUMN_VIEWS))
            )
        }
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            val postVideoId = getLongOrNull(getColumnIndex(PostColumns.COLUMN_POST_VIDEO_ID))
            val video = if (postVideoId == null) null else getVideo(postVideoId)
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shareCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
                sharedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                views = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS)),
                video = video
            )
        }
    }
}
