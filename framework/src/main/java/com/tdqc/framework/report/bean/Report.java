package com.tdqc.framework.report.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据上报节点数据结构
 * Created by chenyen on 2017/6/21.
 */
@Table("data_report")
public class Report {

    /**Report类型枚举*/
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        /**事件*/
        String EVENT = "event";
    }

    public Report(String parentNode) {
        reqTime = System.currentTimeMillis();
        this.parentNode = parentNode;
    }

    public Report(String parentNode, String sonNode) {
        reqTime = System.currentTimeMillis();
        this.parentNode = parentNode;
        this.sonNode = sonNode;
    }

    @Column("id")
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public int id;
    @Column("userId")
    public String userId = "";
    @Column("reqTime")
    public long reqTime;
    @Column("type")
    @Type
    public String type = Type.EVENT;
    @Column("parentNode")
    public String parentNode = "";
    @Column("sonNode")
    public String sonNode = "";
    /**备用字段*/
    @Column("backUpLong")
    public long backUpLong = 0;
    /**备用字段*/
    @Column("backUpString1")
    public String backUpString1 = "";
    /**备用字段*/
    @Column("backUpString2")
    public String backUpString2 = "";

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", time=" + reqTime +
                ", type='" + type + '\'' +
                ", parentNode='" + parentNode + '\'' +
                ", sonNode='" + sonNode + '\'' +
                '}';
    }

}
