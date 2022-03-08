package com.len.scannerproject

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aisier.architecture.base.BaseActivity
import com.aisier.network.toast
import com.len.scannerproject.databinding.ActivityMainBinding
import com.len.scannerproject.view.IOSBottomDialog
import com.len.scannerproject.config.IntentConfig
import com.len.scannerproject.ui.IdCardActivity
import com.len.scannerproject.ui.TextEditingActivity
import com.len.scannerproject.viewmodel.CharacterRecognitionViewModel
import com.len.scannerproject.viewmodel.IdCardViewModel
import com.tdqc.util.Base64Utils
import com.tdqc.util.PhotoUtils
import com.tdqc.util.To
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import java.io.File
import java.util.HashMap


class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModels<CharacterRecognitionViewModel>()
    private val idCardViewModel by viewModels<IdCardViewModel>()

    var imagePath=""

    override fun init() {
        binding.llCamera.setOnClickListener {
            initPermission()
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.textLiveData.observeState(this) {
            onSuccess {
                startActivity(Intent(this@MainActivity,TextEditingActivity::class.java).putExtra("ContentDTO",it))
            }
            onFailed { i, s ->
                toast("onFailed$s")
            }
            onException {
                toast(it.toString())
            }
            onEmpty {
                toast("onEmpty")
            }
            onComplete {

            }
        }

    }

    /**识别文字*/
    private fun getAccurateBasic(base64 : String){
        viewModel.getText(base64)
    }

    /**申请权限*/
    private fun initPermission() {
        /** 请求码:SD卡权限  */
        binding.ivCamera.post(Runnable { /*精确执行splash_time（UI绘制完成后再开始计算）*/
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
            .addSheetItem("扫描文字", null) {
                var pathname = "avatar.png"
                imageUri = PhotoUtils.takePhoto(this, pathname, TAKE_PHOTO)

            }.addSheetItem("识别身份证", null) {
                startActivity(Intent(this@MainActivity,IdCardActivity::class.java))
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
                        val base64=Base64Utils.bitmapToBase64(ysbitmap)
                        //getIdCard(base64,"front")
                        getAccurateBasic(base64)
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
            val file = File(imagePath)
            //            picmap.put("avatar.png", file)
            //            SubmitPic()
            val ysbitmap: Bitmap? = PhotoUtils.zipPhoto(imagePath)
            if (ysbitmap != null) {
                val base64=Base64Utils.bitmapToBase64(ysbitmap)
                this.imagePath=imagePath
                // ysbitmap.recycle()
            } else {
                To.s("获取图片失败")
            }
        } else {
            To.s("获取图片失败")
        }
    }


}