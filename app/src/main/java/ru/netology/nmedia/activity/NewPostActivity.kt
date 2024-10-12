package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.focusAndShowKeyboard


class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.edit.requestFocus()

        intent?.let {
            val text: String? = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text == null) {
                binding.edit.hint = getString(R.string.post_text)
                binding.editPreview.text = ""
                binding.editGroup.visibility = View.GONE
                binding.edit.focusAndShowKeyboard()
            } else {
                binding.editMessage.setText(R.string.edit_message)
                binding.editPreview.text = text
                binding.editGroup.visibility = Group.VISIBLE
                binding.edit.setText(text)
                binding.edit.focusAndShowKeyboard()
            }
        }


        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
        binding.editClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    object NewPostResultContract : ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, input: String?) =
            Intent(context, NewPostActivity::class.java).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    input
                )
            }

        override fun parseResult(resultCode: Int, intent: Intent?): String? =
            if (resultCode == RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                null
            }
    }
}