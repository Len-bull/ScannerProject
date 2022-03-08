package com.len.scannerproject.viewmodel

import com.aisier.architecture.base.BaseViewModel
import com.aisier.network.observer.StateLiveData
import com.len.scannerproject.bean.IdCardBackBean
import com.len.scannerproject.bean.IdCardBean
import com.len.scannerproject.repository.IdCardRepository

class IdCardViewModel : BaseViewModel() {
    private val repository by lazy { IdCardRepository() }
    val idCardTextLiveData = StateLiveData<IdCardBean>()
    val idCardBackTextLiveData = StateLiveData<IdCardBackBean>()
    fun getIdCardText(params : Map<String,String>){
        launchWithLoading(
            requestBlock = {repository.getIdCardText(params)},
            resultCallback = {
                idCardTextLiveData.value=it
            }
        )
    }

    fun getIdCardBackText(params : Map<String,String>){
        launchWithLoading(
            requestBlock = {repository.getIdCardBackText(params)},
            resultCallback = {
                idCardBackTextLiveData.value=it
            }
        )
    }


}