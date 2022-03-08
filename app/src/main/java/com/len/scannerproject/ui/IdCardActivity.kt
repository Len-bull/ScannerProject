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
            To.s("复制成功")
        }
        viewBinding.tvIdNumber.setOnClickListener {
            copy.text=viewBinding.tvIdNumber.text
            To.s("复制成功")
        }
    }

    private fun initObserver() {
        idCardViewModel.idCardTextLiveData.observeState(this) {
            onSuccess {
                Log.d("test","${it.wordsResult.姓名.words}")
                val data=it.wordsResult
                data.run {
                    viewBinding.tvName.text= "${this.姓名.words}"
                    viewBinding.tvSex.text= "${this.性别.words}"
                    viewBinding.tvNation.text="${this.民族.words}"
                    viewBinding.tvBirthday.text= "${this.出生.words.substring(0..3)} 年 ${this.出生.words.substring(4..5)} 月 ${this.出生.words.substring(6..7)} 日"
                    viewBinding.tvAddress.text= "${this.住址.words}"
                    viewBinding.tvIdNumber.text= "${this.公民身份号码.words}"
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
                    viewBinding.tvOrganization.text="${this.签发机关.words}"
                    viewBinding.tvDate.text="${this.签发日期.words.substring(0..3)}." +
                            "${this.签发日期.words.substring(4..5)}.${this.签发日期.words.substring(6..7)}" +
                            "-${this.失效日期.words.substring(0..3)}."+
                    "${this.失效日期.words.substring(4..5)}.${this.失效日期.words.substring(6..7)}"
                }
            }
            onFailed { code, msg ->
                toast("onFailed$msg")
            }
        }

    }

    /**识别身份证*/
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

    /**申请权限*/
    private fun initPermission() {
        /** 请求码:SD卡权限  */
        viewBinding.imageIdCard.post(Runnable { /*精确执行splash_time（UI绘制完成后再开始计算）*/
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
        To.s("您未允许食安快线获取相应权限，您可在系统设置中开启")
    }

    var imageUri: Uri? = null
    private val TAKE_PHOTO = 1
    private val CHOOSE_PHOTO = 2
    var picmap: HashMap<String, File> = HashMap()

    /** 弹出选择  */
    private fun showAlterHeadDialog() {
        IOSBottomDialog(this).setCancelable(true).setCanceledOnTouchOutside(true).setTitle("选择图片")
            .addSheetItem("相机", null) {
                var pathname = "avatar.png"
                imageUri = PhotoUtils.takePhoto(this, pathname, TAKE_PHOTO)

            }.addSheetItem("相册", null) {
                PhotoUtils.startPickPhotoActivity(this, CHOOSE_PHOTO)
            }.show()
    }

    /**选择图片回调*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == RESULT_OK) {
                //将拍摄的照片显示出来
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
                        To.s("获取图片失败")
                    }
                }
            }
            CHOOSE_PHOTO -> if (resultCode == RESULT_OK) {
                //判断手机系统版本号
                var imagePath=""
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上系统使用这个方法处理图片
                    imagePath = PhotoUtils.handleImageOnKitKat(data)
                    diaplayImage(imagePath)
                } else {
                    //4.4以下系统使用这个放出处理图片
                    imagePath = PhotoUtils.handleImageBeforeKitKat(data)
                    diaplayImage(imagePath)
                }
            }
            else -> {
            }
        }
    }

    /** 依据图片地址压缩生成图片并转码  */
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
                To.s("获取图片失败")
            }
        } else {
            To.s("获取图片失败")
        }
    }

}