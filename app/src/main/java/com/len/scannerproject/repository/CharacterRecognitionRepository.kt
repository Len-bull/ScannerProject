package com.len.scannerproject.repository

import com.aisier.network.base.BaseRepository
import com.aisier.network.entity.ApiResponse
import com.len.scannerproject.bean.AccurateBasicBean
import com.len.scannerproject.http.RetrofitClient

class CharacterRecognitionRepository : BaseRepository(){
    private val service by lazy { RetrofitClient.service }
    suspend fun getTextFromNet(base64:String) : ApiResponse<AccurateBasicBean>{
        return executeHttp {
            service.requestAccurateBasic(base64)
        }
    }
}