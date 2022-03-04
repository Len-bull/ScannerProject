package com.tdqc.framework.http;


import android.util.Log;

import com.tdqc.util.L;
import com.tdqc.util.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Http请求日志输出器
 * Created by chenyen on 2017/2/23.
 */
public class TdqcLogInterceptor implements Interceptor {

    private final String TAG = "HTTP";

    @Override
    public Response intercept(Chain chain) throws IOException {
        long start = System.currentTimeMillis();
        Request request = chain.request();
        /*请求参数输出*/
        if (request != null) {
            Log.i(TAG, "request --> " + request.url() + "\n" + getRequestBody(request));
        }else{
            Log.i(TAG, "request --> 为空");
        }
        /*执行请求，得到response*/
        Response response = null;
        try{
            response = chain.proceed(request);
        }catch (IOException e){
            if(StringUtils.contains(e.getMessage(),"Socket closed")){ /*请求被取消*/
                L.i(TAG,"request canceled --> " + request.url());
            }else{
                L.e(TAG,"http error: " + request.url(),e); /*其他错误*/
            }
            throw e;
        }
        try{ /*输出response*/
            ResponseBody body = response.newBuilder().build().body();
            MediaType mediaType = response.newBuilder().build().body().contentType();
            if (mediaType != null && isText(mediaType)) {
                String resp = body.string();
                if(!response.isSuccessful()){
                    L.e(TAG, "response --> \n" + "url:" + response.request().url() + "\n" + resp); /*请求失败*/
                }else{
                    L.i(TAG, "response --> \n" + "spend time:" + (System.currentTimeMillis() - start) + "\n" + "url:" + response.request().url() + "\n" + resp); /*请求成功*/
                }
                body = ResponseBody.create(mediaType, resp);
                return response.newBuilder().body(body).build();
            }else{
                L.e(TAG,"error " + response.code() + " " + response.message());
            }
        }catch (Exception e){
            L.e(TAG,e);
        }
        return response;
    }

    /**获取Request中的参数*/
    private String getRequestBody(final Request request) {
        try {
            Buffer buffer = new Buffer();
            request.newBuilder().build().body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "";
        }
    }

    /**判断是否是文字类型的返回值*/
    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

}
