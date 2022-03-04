package com.len.scannerproject.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()//隐藏标题栏
        window.setFlags(WindowManager.LayoutParams.ALPHA_CHANGED, WindowManager.LayoutParams.ALPHA_CHANGED)
        init()
    }
    protected abstract fun init()
}