package com.len.scannerproject.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IdCardBean implements Serializable {
    @SerializedName("error_msg")
    private String errorMsg;
    @SerializedName("error_code")
    private Integer errorCode;
    @SerializedName("words_result")
    private WordsResultDTO wordsResult;
    @SerializedName("words_result_num")
    private Integer wordsResultNum;
    @SerializedName("idcard_number_type")
    private Integer idcardNumberType;
    @SerializedName("image_status")
    private String imageStatus;
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

    public WordsResultDTO getWordsResult() {
        return wordsResult;
    }

    public void setWordsResult(WordsResultDTO wordsResult) {
        this.wordsResult = wordsResult;
    }

    public Integer getWordsResultNum() {
        return wordsResultNum;
    }

    public void setWordsResultNum(Integer wordsResultNum) {
        this.wordsResultNum = wordsResultNum;
    }

    public Integer getIdcardNumberType() {
        return idcardNumberType;
    }

    public void setIdcardNumberType(Integer idcardNumberType) {
        this.idcardNumberType = idcardNumberType;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public static class WordsResultDTO implements Serializable{
        @SerializedName("姓名")
        private 姓名DTO 姓名;
        @SerializedName("民族")
        private 民族DTO 民族;
        @SerializedName("住址")
        private 住址DTO 住址;
        @SerializedName("公民身份号码")
        private 公民身份号码DTO 公民身份号码;
        @SerializedName("出生")
        private 出生DTO 出生;
        @SerializedName("性别")
        private 性别DTO 性别;

        public 姓名DTO get姓名() {
            return 姓名;
        }

        public void set姓名(姓名DTO 姓名) {
            this.姓名 = 姓名;
        }

        public 民族DTO get民族() {
            return 民族;
        }

        public void set民族(民族DTO 民族) {
            this.民族 = 民族;
        }

        public 住址DTO get住址() {
            return 住址;
        }

        public void set住址(住址DTO 住址) {
            this.住址 = 住址;
        }

        public 公民身份号码DTO get公民身份号码() {
            return 公民身份号码;
        }

        public void set公民身份号码(公民身份号码DTO 公民身份号码) {
            this.公民身份号码 = 公民身份号码;
        }

        public 出生DTO get出生() {
            return 出生;
        }

        public void set出生(出生DTO 出生) {
            this.出生 = 出生;
        }

        public 性别DTO get性别() {
            return 性别;
        }

        public void set性别(性别DTO 性别) {
            this.性别 = 性别;
        }

        public static class 姓名DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }

        public static class 民族DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }

        public static class 住址DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }

        public static class 公民身份号码DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }

        public static class 出生DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }

        public static class 性别DTO implements Serializable{
            @SerializedName("location")
            private LocationDTO location;
            @SerializedName("words")
            private String words;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationDTO implements Serializable{
                @SerializedName("top")
                private Integer top;
                @SerializedName("left")
                private Integer left;
                @SerializedName("width")
                private Integer width;
                @SerializedName("height")
                private Integer height;

                public Integer getTop() {
                    return top;
                }

                public void setTop(Integer top) {
                    this.top = top;
                }

                public Integer getLeft() {
                    return left;
                }

                public void setLeft(Integer left) {
                    this.left = left;
                }

                public Integer getWidth() {
                    return width;
                }

                public void setWidth(Integer width) {
                    this.width = width;
                }

                public Integer getHeight() {
                    return height;
                }

                public void setHeight(Integer height) {
                    this.height = height;
                }
            }
        }
    }
}
