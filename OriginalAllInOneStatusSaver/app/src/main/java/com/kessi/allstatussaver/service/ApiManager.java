package com.kessi.allstatussaver.service;

import android.app.Activity;

import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ApiManager {


    public void getInsInfo(final DisposableObserver observer, String URL) {

       RetroClientApi.getInstance(mActivity).getRetro().getInsData(URL,"",
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    private static Activity mActivity;
    private static ApiManager apiManager;
    public static ApiManager getInstance(Activity activity) {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        mActivity = activity;
        return apiManager;
    }
}