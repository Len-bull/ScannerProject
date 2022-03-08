package com.len.scannerproject.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AccurateBasicBean implements Serializable {
    @SerializedName("error_msg")
    private String errorMsg;
    @SerializedName("error_code")
    private Integer errorCode;
    @SerializedName("words_result")
    private List<WordsResultDTO> wordsResult;
    @SerializedName("words_result_num")
    private Integer wordsResultNum;
    @SerializedName("log_id")
    private Long logId;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<WordsResultDTO> getWordsResult() {
        return wordsResult;
    }

    public void setWordsResult(List<WordsResultDTO> wordsResult) {
        this.wordsResult = wordsResult;
    }

    public Integer getWordsResultNum() {
        return wordsResultNum;
    }

    public void setWordsResultNum(Integer wordsResultNum) {
        this.wordsResultNum = wordsResultNum;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public static class WordsResultDTO implements Serializable {
        @SerializedName("words")
        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
