package com.sa.farmersscreenba.net

import com.aisier.network.entity.ApiResponse
import com.len.scannerproject.bean.AccurateBasicBean
import com.len.scannerproject.bean.IdCardBean
import retrofit2.http.*

interface HttpApiInterface {
    /**文字识别*/
    @FormUrlEncoded
    @POST("accurate_basic")
    suspend fun requestAccurateBasic(@Field("image") image:String): ApiResponse<AccurateBasicBean>
    /**身份证识别 Map方式传参*/
    @FormUrlEncoded
    @POST("idcard")
    suspend fun requestIdCard(@FieldMap params: Map<String,String>):ApiResponse<IdCardBean>
}