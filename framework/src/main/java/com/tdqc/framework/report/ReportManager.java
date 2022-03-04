package com.tdqc.framework.report;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.tdqc.framework.LiteOrmManager;
import com.tdqc.framework.report.bean.Report;
import com.tdqc.util.SpUtils;
import com.tdqc.util.StringUtils;
import com.tdqc.util.TimeUtils;

import java.util.List;

/**
 * 数据上报模块管理器
 * Created by chenyen on 2017/6/21.
 */
public class ReportManager {

    /** 单例 */
    private static volatile ReportManager instance = null;
    private Context appContext = null;
    /**最小上报时间间隔*/
    public final long minUploadSpace = TimeUtils.TimeType.MIN.getTime() * 30;
    /**上报数量阈值：数据超过此数量则忽略最小上报时间间隔直接上报*/
    public final int uploadSizeThreshold = 300;
    /**数据库存储的最大数据，超过此数量则需要做数据删除操作避免数据量过大（数据量越大上传的失败率越高）*/
    public final int maxReportSize = 5000;
    /**存储上一次上报时间的SP key*/
    public final String SPKEY_LAST_UPLOAD_TIME = "LAST_UPLOAD_REPORT_TIME";
    /**获取用户Id回调*/
    private GetUserIdCall getUserIdCall;

    public static ReportManager get() {
        ReportManager tempInst = instance;
        if (tempInst == null) {
            synchronized (ReportManager.class) {
                tempInst = instance;
                if (tempInst == null) {
                    tempInst = new ReportManager();
                    instance = tempInst;
                }
            }
        }
        return tempInst;
    }

    private ReportManager() {}

    /**获取UserId回调*/
    public interface GetUserIdCall{
        @Nullable
        String getUserId();
    }

    public void init(@NonNull Application appContext, @Nullable GetUserIdCall getUserIdCall){
        this.appContext = appContext;
        this.getUserIdCall = getUserIdCall;
    }

    /**插入数据*/
    public synchronized void insertReport(@NonNull Report reportItem){
        if(getUserIdCall != null){
            String userId = getUserIdCall.getUserId();
            if(!StringUtils.isEmpty(userId)){
                reportItem.userId = userId;
            }
        }
        LiteOrmManager.getLiteOrm(appContext).save(reportItem);
    }

    /**
     * 获取上报用的reportList
     * @Return 如果没有可上报的数据或是距离上次上报时间过近则返回null，无需上报。否则返回需要上报的reportList。
     * */
    @Nullable
    public synchronized List<Report> getUploadReportList(){
        List<Report> reportList = queryReportList();
        if(reportList == null || reportList.size() == 0){
            return null;
        }
        if(reportList.size() >= uploadSizeThreshold){
            if(reportList.size() >= maxReportSize){ /*数据量过大*/
                cleanOldData();
            }
            return reportList;
        }
        return System.currentTimeMillis() - SpUtils.getLong(SPKEY_LAST_UPLOAD_TIME,0) > minUploadSpace ? reportList:null;
    }

    /**上报成功调用*/
    public synchronized void uploadSuccess(){
        SpUtils.putLong(SPKEY_LAST_UPLOAD_TIME,System.currentTimeMillis());
        cleanReportList();
    }

    /**读取缓存的Report*/
    private List<Report> queryReportList(){
        return LiteOrmManager.getLiteOrm(appContext).query(Report.class);
    }

    /**清空缓存的Report*/
    private void cleanReportList(){
        LiteOrmManager.getLiteOrm(appContext).delete(Report.class);
    }

    /**删除老旧数据，只留下最大数据量的一半数据*/
    private void cleanOldData() {
        List<Report> anchor = LiteOrmManager.getLiteOrm(appContext).query(new QueryBuilder<>(Report.class).limit(maxReportSize/2,1).appendOrderDescBy("id"));
        if(anchor != null && anchor.size() > 0){
            LiteOrmManager.getLiteOrm(appContext).delete(new WhereBuilder(Report.class).lessThan("id", anchor.get(0).id));
        }
    }

}
