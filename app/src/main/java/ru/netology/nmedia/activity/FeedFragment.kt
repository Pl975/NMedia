package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostAdapter

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostAdapter(object : PostIteractionListener(viewModel, requireContext()) {

            override fun onEdit(post: Post) {
                super.onEdit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content })
            }
            override fun onPostOpen(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply { textArg = post.id.toString() })
            }
        })


        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root


    }
}






