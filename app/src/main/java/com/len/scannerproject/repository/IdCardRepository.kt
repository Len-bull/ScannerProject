package com.len.scannerproject.repository

import com.aisier.network.base.BaseRepository
import com.aisier.network.entity.ApiResponse
import com.len.scannerproject.bean.IdCardBackBean
import com.len.scannerproject.bean.IdCardBean
import com.len.scannerproject.http.RetrofitClient

class IdCardRepository : BaseRepository() {
    private val service by lazy { RetrofitClient.service }
    suspend fun getIdCardText(params : Map<String,String>):ApiResponse<IdCardBean> {
        return executeHttp {
            service.requestIdCard(params)
        }
    }

    suspend fun getIdCardBackText(params : Map<String,String>):ApiResponse<IdCardBackBean> {
        return executeHttp {
            service.requestIdCardBack(params)
        }
    }

//    suspend fun getIdCardText(access_token:String,image : String,id_card_side :String):ApiResponse<IdCardBean> {
//        return executeHttp {
//            service.requestIdCard(access_token,image,id_card_side)
//        }
//    }
}