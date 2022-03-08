package com.len.scannerproject.http

import com.aisier.network.base.BaseRetrofitClient
import com.len.scannerproject.config.Config
import com.sa.farmersscreenba.net.HttpApiInterface
import okhttp3.OkHttpClient

object RetrofitClient : BaseRetrofitClient(){
    val service by lazy { getService(HttpApiInterface::class.java,Config.REQUEST_URL) }
    override fun handleBuilder(builder: OkHttpClient.Builder) = Unit
}