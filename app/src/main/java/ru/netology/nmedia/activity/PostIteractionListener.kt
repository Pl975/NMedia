package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


open class PostIteractionListener(
    private val viewModel: PostViewModel,
    private val context: Context
) : OnInteractionListener {
    override fun onEdit(post: Post) {
        viewModel.edit(post)
    }
    override fun onLike(post: Post) {
        viewModel.likeById(post.id)
    }
    override fun onRemove(post: Post) {
        viewModel.removeById(post.id)
    }
    override fun onShare(post: Post) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, post.content)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.chooser_share_post))
        context.startActivity(chooser)
        viewModel.shareById(post.id)
    }
    override fun onImageVideo(post: Post) {
        playVideo(post)
    }
    override fun onPlayVideo(post: Post) {
        playVideo(post)
    }
    override fun onPostOpen(post: Post) {
    }
    fun playVideo(post: Post) {
        if (post.video != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video.url))
            context.startActivity(intent)
        }
    }
}