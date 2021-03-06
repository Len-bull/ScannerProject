package com.len.scannerproject.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.ClipboardManager
import android.util.Log
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aisier.architecture.base.BaseActivity
import com.aisier.network.toast
import com.len.scannerproject.R
import com.len.scannerproject.bean.IdCardBean
import com.len.scannerproject.config.IntentConfig
import com.len.scannerproject.databinding.ActivityIdCardBinding
import com.len.scannerproject.view.IOSBottomDialog
import com.len.scannerproject.viewmodel.IdCardViewModel
import com.tdqc.util.Base64Utils
import com.tdqc.util.PhotoUtils
import com.tdqc.util.To
import com.tdqc.util.glide.GlideLoad
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import java.io.File
import java.util.HashMap


class IdCardActivity : BaseActivity(R.layout.activity_id_card) {
    private val viewBinding by viewBinding(ActivityIdCardBinding::bind)
    private val idCardViewModel by viewModels<IdCardViewModel>()
    private var idCardSide= "front"
    private var frontBase64= ""
    private var backBase64= ""
    override fun init() {
        initObserver()
        viewBinding.imageIdCard.setOnClickListener {
            idCardSide="front"
            initPermission()
        }
        viewBinding.imageIdCardBack.setOnClickListener {
            idCardSide= "back"
            initPermission()
        }

        viewBinding.btStart.setOnClickListener {
            if (frontBase64.isNotEmpty()){
                getIdCard(frontBase64,"front")
            }
            if (backBase64.isNotEmpty()){
                getIdCard(backBase64,"back")
            }

        }

        var copy: ClipboardManager = this
            .getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        viewBinding.tvName.setOnClickListener {
            copy.text=viewBinding.tvName.text
            To.s("????????????")
        }
        viewBinding.tvIdNumber.setOnClickListener {
            copy.text=viewBinding.tvIdNumber.text
            To.s("????????????")
        }
    }

    private fun initObserver() {
        idCardViewModel.idCardTextLiveData.observeState(this) {
            onSuccess {
                Log.d("test","${it.wordsResult.??????.words}")
                val data=it.wordsResult
                data.run {
                    viewBinding.tvName.text= "${this.??????.words}"
                    viewBinding.tvSex.text= "${this.??????.words}"
                    viewBinding.tvNation.text="${this.??????.words}"
                    viewBinding.tvBirthday.text= "${this.??????.words.substring(0..3)} ??? ${this.??????.words.substring(4..5)} ??? ${this.??????.words.substring(6..7)} ???"
                    viewBinding.tvAddress.text= "${this.??????.words}"
                    viewBinding.tvIdNumber.text= "${this.??????????????????.words}"
                }
            }
            onFailed { code, msg ->
                toast("onFailed$msg")
            }
            onException {
                toast(it.toString())
            }
            onEmpty {

            }
            onComplete {

            }
        }

        idCardViewModel.idCardBackTextLiveData.observeState(this){
            onSuccess {
                val data= it.wordsResult
                data.run {
                    viewBinding.tvOrganization.text="${this.????????????.words}"
                    viewBinding.tvDate.text="${this.????????????.words.substring(0..3)}." +
                            "${this.????????????.words.substring(4..5)}.${this.????????????.words.substring(6..7)}" +
                            "-${this.????????????.words.substring(0..3)}."+
                    "${this.????????????.words.substring(4..5)}.${this.????????????.words.substring(6..7)}"
                }
            }
            onFailed { code, msg ->
                toast("onFailed$msg")
            }
        }

    }

    /**???????????????*/
    private fun getIdCard(base64 : String, id_card_side : String){
        var params= mutableMapOf<String,String>()
        params["access_token"]= "24.22cdd7e6c13af94e50e0f9281b303b1b.2592000.1648266061.282335-24081649"
        params["image"]= base64
        params["id_card_side"]= id_card_side
        if (id_card_side=="front"){
            idCardViewModel.getIdCardText(params)
        }else{
            idCardViewModel.getIdCardBackText(params)
        }
    }

    /**????????????*/
    private fun initPermission() {
        /** ?????????:SD?????????  */
        viewBinding.imageIdCard.post(Runnable { /*????????????splash_time???UI?????????????????????????????????*/
            PermissionGen.with(this).addRequestCode(IntentConfig.REQUEST_CODE_PERMISSION_SD)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).request()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @PermissionSuccess(requestCode = IntentConfig.REQUEST_CODE_PERMISSION_SD)
    fun requestPermissionSuccess() {
        showAlterHeadDialog()
//        var pathname = "avatar.png"
//        imageUri = PhotoUtils.takePhoto(this, pathname, TAKE_PHOTO)
    }

    @PermissionFail(requestCode = IntentConfig.REQUEST_CODE_PERMISSION_SD)
    private fun requestPermissionFail() {
        To.s("???????????????????????????????????????????????????????????????????????????")
    }

    var imageUri: Uri? = null
    private val TAKE_PHOTO = 1
    private val CHOOSE_PHOTO = 2
    var picmap: HashMap<String, File> = HashMap()

    /** ????????????  */
    private fun showAlterHeadDialog() {
        IOSBottomDialog(this).setCancelable(true).setCanceledOnTouchOutside(true).setTitle("????????????")
            .addSheetItem("??????", null) {
                var pathname = "avatar.png"
                imageUri = PhotoUtils.takePhoto(this, pathname, TAKE_PHOTO)

            }.addSheetItem("??????", null) {
                PhotoUtils.startPickPhotoActivity(this, CHOOSE_PHOTO)
            }.show()
    }

    /**??????????????????*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == RESULT_OK) {
                //??????????????????????????????
                if (imageUri != null) {
                    val filePathForN = PhotoUtils.getFilePath(this, imageUri)
                    val ysbitmap: Bitmap? = PhotoUtils.zipPhoto(filePathForN)
                    if (ysbitmap != null) {
                        val base64= Base64Utils.bitmapToBase64(ysbitmap)
                        if (idCardSide=="front"){
                            frontBase64=base64
                            GlideLoad.load(filePathForN,viewBinding.imageIdCard)
                        }else{
                            backBase64=base64
                            GlideLoad.load(filePathForN,viewBinding.imageIdCardBack)
                        }
                        // ysbitmap.recycle()
                    } else {
                        To.s("??????????????????")
                    }
                }
            }
            CHOOSE_PHOTO -> if (resultCode == RESULT_OK) {
                //???????????????????????????
                var imagePath=""
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4?????????????????????????????????????????????
                    imagePath = PhotoUtils.handleImageOnKitKat(data)
                    diaplayImage(imagePath)
                } else {
                    //4.4??????????????????????????????????????????
                    imagePath = PhotoUtils.handleImageBeforeKitKat(data)
                    diaplayImage(imagePath)
                }
            }
            else -> {
            }
        }
    }

    /** ?????????????????????????????????????????????  */
    private fun diaplayImage(imagePath: String) {
        if (imagePath.isNotEmpty()) {
            val ysbitmap: Bitmap? = PhotoUtils.zipPhoto(imagePath)
            if (ysbitmap != null) {
                val base64= Base64Utils.bitmapToBase64(ysbitmap)
                if (idCardSide=="front"){
                    frontBase64=base64
                    GlideLoad.load(imagePath,viewBinding.imageIdCard)
                }else{
                    backBase64=base64
                    GlideLoad.load(imagePath,viewBinding.imageIdCardBack)
                }
                // ysbitmap.recycle()
            } else {
                To.s("??????????????????")
            }
        } else {
            To.s("??????????????????")
        }
    }

}