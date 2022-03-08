package com.len.scannerproject.ui

import by.kirich1409.viewbindingdelegate.viewBinding
import com.aisier.architecture.base.BaseActivity
import com.len.scannerproject.R
import com.len.scannerproject.bean.AccurateBasicBean
import com.len.scannerproject.databinding.ActivityTextEditingBinding

class TextEditingActivity : BaseActivity(R.layout.activity_text_editing) {
    private val binding by viewBinding(ActivityTextEditingBinding::bind)

    override fun init() {
        var contentDTO=intent.getSerializableExtra("ContentDTO")
        if (contentDTO==null){
            binding.etContent.setText("")
        }else{
            val content = contentDTO as AccurateBasicBean
            var value=""
            for (bean in content.wordsResult){
                if (value==""){
                    value=bean.words
                }else{
                    value=value+"\r\n${bean.words}"
                }

            }
            binding.etContent.setText(value)
        }
    }
}