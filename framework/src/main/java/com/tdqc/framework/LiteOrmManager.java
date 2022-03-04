package com.tdqc.framework;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.tdqc.util.L;
import com.tdqc.util.PacketUtils;


/**
 * LiteOrm管理
 * Created by chenyen on 2017/6/21.
 */
public class LiteOrmManager {

    private static LiteOrm liteOrm;
    /**如果需要设置config请在调用getLiteOrm()之前设置好，否则无效*/
    public static DataBaseConfig config;

    public static synchronized LiteOrm getLiteOrm(Context context){
        if (liteOrm == null) {
            if(config == null){
                liteOrm = LiteOrm.newSingleInstance(context, PacketUtils.getPacketName() + ".db");
                liteOrm.setDebugged(L.isPrintLog());
            }else{
                liteOrm = LiteOrm.newSingleInstance(config);
            }
        }
        return liteOrm;
    }

}
