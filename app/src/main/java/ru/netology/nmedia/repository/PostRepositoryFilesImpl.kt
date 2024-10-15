package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {

    companion object {
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()
    }

    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
                nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
                data.value = posts
            }
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (!it.likedByMe) it.copy(
                    likedByMe = !it.likedByMe,
                    likesCount = it.likesCount + 1
                )
                else it.copy(likedByMe = !it.likedByMe, likesCount = it.likesCount - 1)
            }
        }
        data.value = posts

    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                sharedByMe = !it.sharedByMe,
                shareCount = it.shareCount + 1
            )
        }
        data.value = posts

    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts

    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, published = "Now", author = "Netology")) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
        return
    }

    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

}