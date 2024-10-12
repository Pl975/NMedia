package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onShare(post: Post)
    fun onImageVideo(post: Post)
    fun onPlayVideo(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,

    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            ivLikes.isChecked = post.likedByMe
            ivLikes.text = PostService.ConvertCountToShortString(post.likesCount)
            ivShares.isChecked = post.sharedByMe
            ivShares.text = PostService.ConvertCountToShortString(post.shareCount)
            ivVisibility.text = PostService.ConvertCountToShortString(post.visibilityCount)

            layoutVideo.visibility = View.GONE
            if (post.video != null) {
                layoutVideo.visibility = View.VISIBLE
                txtName.text = post.video.name
                txtViewCount.text = post.video.views.toString()
            }

            playVideo.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }

            imageVideo.setOnClickListener {
                onInteractionListener.onImageVideo(post)
            }


            ivLikes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            ivShares.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem

}