package com.len.scannerproject.ui

import android.content.Context
import android.text.ClipboardManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aisier.architecture.base.BaseActivity
import com.len.scannerproject.R
import com.len.scannerproject.bean.IdCardBean
import com.len.scannerproject.databinding.ActivityIdCardBinding
import com.tdqc.util.To
import com.tdqc.util.glide.GlideLoad


class IdCardActivity : BaseActivity(R.layout.activity_id_card) {
    private val viewBinding by viewBinding(ActivityIdCardBinding::bind)
    //val copy=this.getSystemService(Context.CLIPBOARD_SERVICE)
    override fun init() {
        val imagePath=intent.getStringExtra("imagePath")
        val contentDTO=intent.getSerializableExtra("ContentDTO")
        if (contentDTO!=null){
            val data=contentDTO as IdCardBean.WordsResultDTO
            data.run {
                viewBinding.tvName.text= "姓名  ${this.姓名.words}"
                viewBinding.tvSexAndNation.text= "性别  ${this.性别.words}    民族 ${this.民族.words}"
                viewBinding.tvBirthday.text= "出生:  ${this.出生.words.substring(0..3)} 年 ${this.出生.words.substring(4..5)} 月 ${this.出生.words.substring(6..7)} 日"
                viewBinding.tvAddress.text= "住址  ${this.住址.words}"
                viewBinding.tvIdNumber.text= "公民身份证号码  ${this.公民身份号码.words}"
            }
        }
        var copy: ClipboardManager = this
            .getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        GlideLoad.load(imagePath,viewBinding.imageIdCard)
        viewBinding.tvName.setOnClickListener {
            copy.text=viewBinding.tvName.text
            To.s("复制成功")
        }
    }

}