package com.kessi.allstatussaver.service;

import android.app.Activity;

import com.google.gson.Gson;
import com.kessi.allstatussaver.interfaces.InterParaAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroClientApi {
    private static Retrofit mRetro = null;
    private static final RetroClientApi mRetroClient = new RetroClientApi();
    private static Activity mContext;
    public static RetroClientApi getInstance(Activity activity) {
        mContext = activity;
        return mRetroClient;
    }

    private RetroClientApi() {
        HttpLoggingInterceptor mCeptor = new HttpLoggingInterceptor();
        mCeptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Response mResp = null;
                        try {
                            Request request = chain.request();
                            mResp = chain.proceed(request);
                            if (mResp.code() == 200) {
                                try {
                                    JSONObject mObject = new JSONObject(mResp.body().string());
                                    String content = mObject.toString();
                                    MediaType mContentType = mResp.body().contentType();
                                    ResponseBody mResBody = ResponseBody.create(mContentType, content);
                                    return mResp.newBuilder().body(mResBody).build();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SocketTimeoutException e) {
                            e.printStackTrace();
                        }
                        return mResp;
                    }
                })
                .addInterceptor(mCeptor)
                .build();
        if (mRetro == null) {
            mRetro = new Retrofit.Builder()
                    .baseUrl("https://www.instagram.com/")
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }
    }
    public InterParaAPI getRetro() {
        return mRetro.create(InterParaAPI.class);
    }
}