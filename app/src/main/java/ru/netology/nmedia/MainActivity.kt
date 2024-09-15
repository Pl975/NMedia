package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 300,
            shareCount = 1_300_000,
            visibilityCount = 700_000,
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published

            if (post.likedByMe) {
                ivLikes.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            likeCount.text = PostService.ConvertCountToShortString(post.likes)
            share.text = PostService.ConvertCountToShortString(post.shareCount)
            visibility.text = PostService.ConvertCountToShortString(post.visibilityCount)

            ivLikes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                ivLikes.setImageResource(
                    if (post.likedByMe) {
                        R.drawable.ic_baseline_favorite_24
                    } else {
                        R.drawable.ic_baseline_favorite_border_24
                    }
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likeCount.text = PostService.ConvertCountToShortString(post.likes)
            }

            ivShares.setOnClickListener {
                post.shareCount++
                share.text = PostService.ConvertCountToShortString(post.shareCount)
            }

            ivVisibility.setOnClickListener {
                post.visibilityCount++
                visibility.text = PostService.ConvertCountToShortString(post.visibilityCount)
            }

        }
    }
}