package com.len.scannerproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.len.scannerproject.R
import com.len.scannerproject.bean.AccurateBasicBean
import com.len.scannerproject.databinding.ActivityTextEditingBinding

class TextEditingActivity : AppCompatActivity() {
    lateinit var binding: ActivityTextEditingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTextEditingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var contentDTO=intent.getSerializableExtra("ContentDTO")
        if (contentDTO==null){
            binding.etContent.setText("")
        }else{
            val content = contentDTO as AccurateBasicBean.DataDTO
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