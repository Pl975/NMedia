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
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class OnePostFragment : Fragment() {
    val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewHolder = PostViewHolder(
            view.postLayout,
            object : PostIteractionListener(
                viewModel, requireContext()
            ) {
                override fun onEdit(post: Post) {
                    super.onEdit(post)
                    findNavController().navigate(
                        R.id.action_onePostFragment_to_newPostFragment,
                        Bundle().apply { textArg = post.content })
                }
            })
        val id = arguments?.textArg?.toLong() ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find {
                it.id == id
            } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }
        return view.root
    }
}

