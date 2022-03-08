package com.len.scannerproject.bean
import com.google.gson.annotations.SerializedName


data class IdCardBackBean(
    @SerializedName("image_status")
    val imageStatus: String,
    @SerializedName("log_id")
    val logId: Long,
    @SerializedName("words_result")
    val wordsResult: WordsResult,
    @SerializedName("words_result_num")
    val wordsResultNum: Int
)

data class WordsResult(
    @SerializedName("失效日期")
    val 失效日期: 失效日期,
    @SerializedName("签发日期")
    val 签发日期: 签发日期,
    @SerializedName("签发机关")
    val 签发机关: 签发机关
)

data class 失效日期(
    @SerializedName("location")
    val location: Location,
    @SerializedName("words")
    val words: String
)

data class 签发日期(
    @SerializedName("location")
    val location: LocationX,
    @SerializedName("words")
    val words: String
)

data class 签发机关(
    @SerializedName("location")
    val location: LocationXX,
    @SerializedName("words")
    val words: String
)

data class Location(
    @SerializedName("height")
    val height: Int,
    @SerializedName("left")
    val left: Int,
    @SerializedName("top")
    val top: Int,
    @SerializedName("width")
    val width: Int
)

data class LocationX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("left")
    val left: Int,
    @SerializedName("top")
    val top: Int,
    @SerializedName("width")
    val width: Int
)

data class LocationXX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("left")
    val left: Int,
    @SerializedName("top")
    val top: Int,
    @SerializedName("width")
    val width: Int
)