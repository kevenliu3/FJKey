package com.fjdynamics.fjkey.base;

import com.uuzuche.lib_zxing.ZApplication;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class BaseApplication extends ZApplication {

    public static final String baseUrl = "http://47.105.133.205:9992";
    public static final  String loginUrl = baseUrl+"/login";
    public  static final String openUrl  = baseUrl+"/openDoor";
    public  static  final String getStateUrl  = baseUrl + "/getState";

    public static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
        okHttpClient = (new OkHttpClient.Builder())
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .build();
    }

}
