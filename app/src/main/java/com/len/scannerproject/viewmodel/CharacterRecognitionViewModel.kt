package com.len.scannerproject.viewmodel
import com.aisier.architecture.base.BaseViewModel
import com.aisier.network.observer.StateLiveData
import com.len.scannerproject.bean.AccurateBasicBean
import com.len.scannerproject.repository.CharacterRecognitionRepository

/**
 *@author : Len
 *time : 2022/03/05
 * des : 扫描文字
 */
class CharacterRecognitionViewModel : BaseViewModel() {
    private val repository by lazy { CharacterRecognitionRepository() }
    val textLiveData=StateLiveData<AccurateBasicBean>()
    fun getText(base64 : String){
        launchWithLoading(
            requestBlock = {repository.getTextFromNet(base64)},
            resultCallback = {
                textLiveData.postValue(it)
            }
        )
    }
}