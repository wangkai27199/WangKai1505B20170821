package com.baway.wangkai;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by DELL on 2017/8/21.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /** CrashHandler实例 */

    private static CrashHandler instance;
    private Context mContext;
    public static final String TAG = "CrashHandler";
    /**
     * @brief 系统默认的UncaughtException处理类
     * */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * @brief 使用Properties来保存设备的信息和错误堆栈信息
     * */
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    /** 获取CrashHandler实例 ,单例模式*/

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }
    /**
     * @brief 初始化,注册Context对象, 获取系统默认的UncaughtException处理器,
     *        设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
//        ScreenManager.getScreenManager().popActivity(Pay.activity);
//         arg0.stop();
//         arg0.destroy();

        String logdir ;
        if(Environment.getExternalStorageDirectory()!=null){
            logdir = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "snda"+File.separator+"log" ;


            File file = new File(logdir);
            boolean mkSuccess;
            if (!file.isDirectory()) {
                mkSuccess = file.mkdirs();
                if (!mkSuccess) {
                    mkSuccess = file.mkdirs();
                }
            }
            try {
                FileWriter fw = new FileWriter(logdir+File.separator+"error.log",true);
                fw.write(new Date()+"\n");
                StackTraceElement[] stackTrace = arg1.getStackTrace();
                fw.write(arg1.getMessage() + "\n");
                for (int i = 0; i < stackTrace.length; i++) {
                    fw.write("file:" + stackTrace[i].getFileName() + " class:" + stackTrace[i].getClassName()
                            + " method:" + stackTrace[i].getMethodName() + " line:" + stackTrace[i].getLineNumber()
                            + "\n");
                }
                fw.write("\n");
                fw.close();
            } catch (IOException e) {
                Log.e("crash handler", "load file failed...", e.getCause());
            }
        }
        arg1.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
