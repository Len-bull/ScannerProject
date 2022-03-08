package com.len.scannerproject.viewmodel

import com.aisier.architecture.base.BaseViewModel
import com.aisier.network.observer.StateLiveData
import com.len.scannerproject.bean.IdCardBean
import com.len.scannerproject.repository.IdCardRepository

class IdCardViewModel : BaseViewModel() {
    private val repository by lazy { IdCardRepository() }
    val idCardTextLiveData = StateLiveData<IdCardBean>()
    fun getIdCardText(params : Map<String,String>){
        launchWithLoading(
            requestBlock = {repository.getIdCardText(params)},
            resultCallback = {
                idCardTextLiveData.value=it
            }
        )
    }

//    fun getIdCardText(access_token:String,image : String,id_card_side :String){
//        launchWithLoading(
//            requestBlock = {repository.getIdCardText(access_token,image,id_card_side)},
//            resultCallback = {
//                idCardTextLiveData.value=it
//            }
//        )
//    }
}