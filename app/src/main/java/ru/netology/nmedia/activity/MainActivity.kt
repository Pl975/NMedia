package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

        }
        )

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (newPost) {
                    binding.recView.smoothScrollToPosition(0)
                }
            }
        }


        binding.recView.adapter = adapter
        binding.save.setOnClickListener {
            val text = binding.edit.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.applyChangesAndSave(text)
            binding.editGroup.visibility = View.GONE
            binding.editedMessage.text = ""
            binding.edit.setText("")
            binding.edit.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.editGroup.visibility = View.VISIBLE
                binding.edit.setText(post.content)
                binding.editedMessage.text = post.content
                binding.edit.focusAndShowKeyboard()
            }
        }
        binding.cancelingEdit.setOnClickListener {
            binding.editGroup.visibility = View.GONE
            binding.editedMessage.text = ""
            binding.edit.setText("")
            binding.edit.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }
    }
}






