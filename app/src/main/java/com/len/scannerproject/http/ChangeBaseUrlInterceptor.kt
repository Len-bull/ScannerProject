package com.sa.farmersscreenba.net

import android.util.Log
import com.len.scannerproject.config.Config
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

object URLConstant{
    const val SERVERIP = "ServerIP"
    const val LOCALIP = "LocalIP"
    const val DOMAIN = "domain"
}
class ChangeBaseUrlInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取request
        var request=chain.request()
        //从request中获取原有的HttpUrl实列 oldHttpUrl
        var oldHttpUrl= request.url
        //获取request的创建者
        var builder=request.newBuilder()
        //从request中获取headers 通过给定的键的url_name
        var headerValues=request.headers(URLConstant.DOMAIN)
        if (headerValues!=null&&headerValues.size>0){
            builder.removeHeader(URLConstant.DOMAIN)
            var headerValue=headerValues[0]
            var newBaseUrl:HttpUrl?
            newBaseUrl=when(headerValue){
                URLConstant.LOCALIP ->{
                    if (Config.ORDER_URL==""){
                        Config.ORDER_URL=Config.REQUEST_URL
                    }
                    Config.ORDER_URL.toHttpUrlOrNull()
                }
                URLConstant.SERVERIP ->{
                    Config.REQUEST_URL.toHttpUrlOrNull()
                }
                else ->{
                    Config.REQUEST_URL.toHttpUrlOrNull()
                }
            }

            newBaseUrl?.run {
                var newHttpUrl=oldHttpUrl.newBuilder().scheme(scheme).host(host).port(port).build()
                Log.i("HTTP","request -->${newHttpUrl}")
                return chain.proceed(builder.url(newHttpUrl).build())
            }

        }
        return chain.proceed(request)
    }
}