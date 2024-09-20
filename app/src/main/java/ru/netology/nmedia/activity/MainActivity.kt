package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.PostService
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                ivLikes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )

                likeCount.text = PostService.ConvertCountToShortString(post.likes)
                share.text = PostService.ConvertCountToShortString(post.shareCount)
                visibility.text = PostService.ConvertCountToShortString(post.visibilityCount)

            }
        }

        binding.ivLikes.setOnClickListener {
            viewModel.like()
        }

        binding.ivShares.setOnClickListener {
            viewModel.share()
        }

        binding.ivVisibility.setOnClickListener {
            viewModel.visibility()
        }
    }

}
