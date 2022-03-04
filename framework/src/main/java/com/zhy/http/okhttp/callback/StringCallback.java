package com.zhy.http.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 请使用{@link com.tdqc.framework.http.GenCallback}
 * Created by zhy on 15/12/14.
 */
@Deprecated
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }
}
