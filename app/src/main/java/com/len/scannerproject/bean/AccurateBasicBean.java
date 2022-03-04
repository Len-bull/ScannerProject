package com.len.scannerproject.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AccurateBasicBean implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("code")
    private Integer code;
    @SerializedName("data")
    private DataDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        @SerializedName("words_result")
        private List<WordsResultDTO> wordsResult;
        @SerializedName("words_result_num")
        private Integer wordsResultNum;
        @SerializedName("log_id")
        private Long logId;

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
}
