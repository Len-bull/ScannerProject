package com.zhy.http.okhttp.callback;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 */
public abstract class FileCallBack extends Callback<File>
{
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    /**上一次同步的进度百分比*/
    private int lastUpdatePresent = 0;


    public FileCallBack(String destFileDir, String destFileName)
    {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }


    @Override
    public File parseNetworkResponse(Response response, int id) throws Exception
    {
        return saveFile(response,id);
    }


    public File saveFile(Response response,final int id) throws IOException
    {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        try
        {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1)
            {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpUtils.getInstance().getDelivery().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int persent = (int) ((finalSum * 1.0f/ total) * 100);
                        if(lastUpdatePresent != persent){ /*避免同一进度重复调用*/
                            lastUpdatePresent = persent;
                            inProgress(persent,total,id);
                        }
                    }
                });
            }
            fos.flush();
            fos.close();
            return file;

        } finally{
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {}
            try{
                response.body().close();
                if (is != null) is.close();
            } catch (Exception e){}
            try{
                if (fos != null) fos.close();
            } catch (Exception e){}
        }
    }


}
