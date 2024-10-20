package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.util.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel


class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val text = arguments?.textArg
        if (text != null) {
            binding.editMessage.setText(R.string.edit_message)
            binding.editPreview.text = text
            binding.editGroup.visibility = Group.VISIBLE
            binding.edit.setText(text)
            binding.edit.focusAndShowKeyboard()
        } else {
            binding.edit.hint = getString(R.string.post_text)
            binding.editPreview.text = ""
            binding.editGroup.visibility = View.GONE
            binding.edit.focusAndShowKeyboard()
        }
        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        binding.editClose.setOnClickListener {
            viewModel.reset()
            findNavController().navigateUp()
        }
        return binding.root
    }
}