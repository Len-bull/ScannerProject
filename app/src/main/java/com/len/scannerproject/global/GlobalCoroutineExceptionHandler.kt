package com.len.scannerproject.global

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class GlobalCoroutineExceptionHandler(private val errCode: Int, private val errMsg: String? = "", private val report: Boolean = false) : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("$errCode","----------------异常警告----------------")
        Log.e("$errCode","---------------------------------------")
        Log.e("$errCode","---------------------------------------")
        val msg =  exception.stackTraceToString()
        Log.e("$errCode","GlobalCoroutineExceptionHandler:${msg}")
        if (report){
            //上报日志
        }
        Log.e("$errCode","---------------------------------------")
        Log.e("$errCode","---------------------------------------")
        Log.e("$errCode","----------------异常警告----------------")
    }
}