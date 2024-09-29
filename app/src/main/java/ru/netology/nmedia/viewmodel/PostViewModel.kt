package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    sharedByMe = false
)
class PostViewModel : ViewModel() {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun applyChangesAndSave(content: String) {
//        edited.value?.let {
//            val text = newText.trim()
//            if (text !=it.content) {
//                repository.save(it.copy(content = text))
//            }
//        }
//        edited.value = empty
//    }
        edited.value?.let {
            if (content!=it.content){
                repository.save(it.copy(content = content))
            }
            edited.value=empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

}
