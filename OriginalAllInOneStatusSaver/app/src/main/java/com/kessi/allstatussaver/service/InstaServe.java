package com.kessi.allstatussaver.service;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.model.Edge;
import com.kessi.allstatussaver.model.EdgeSidecarToChildren;
import com.kessi.allstatussaver.model.InsModel;
import com.kessi.allstatussaver.utils.Utils;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class InstaServe {
    private Activity activity;

    public void getInsData(Activity activity, String urls) {
        this.activity = activity;
        try {
            URL url = new URL(urls);
            String host = url.getHost();
            if (host.equals("www.instagram.com")) {
                startFetch(urls);
            } else {
                Toast.makeText(activity, R.string.invalid, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFetch(String Url) {
        ApiManager mApiManager = ApiManager.getInstance(activity);
        String urlPath = getUrlPath(activity, Url);
        urlPath = urlPath + "?__a=1";
        try {
            if (Utils.isNetworkAvailable(activity)) {
                if (mApiManager != null) {
                    Utils.displayLoader(activity);
                    mApiManager.getInsInfo(disposableObserver, urlPath);
                }
            } else {
                Toast.makeText(activity, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String getUrlPath(Context context, String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null,
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,  R.string.invalid, Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private DisposableObserver<JsonObject> disposableObserver = new DisposableObserver<JsonObject>() {
        String mPhotoUrl;
        String mVideoUrl;
        @Override
        public void onNext(JsonObject versionList) {
            Utils.dismissLoader();
            try {
                Type type = new TypeToken<InsModel>() {
                }.getType();
                InsModel insModel = new Gson().fromJson(versionList.toString(), type);
                EdgeSidecarToChildren toChildren = insModel.getGetMedia().getShortcode_media().getEdge_sidecar_to_children();
                if (toChildren != null) {
                    List<Edge> edgeArrayList = toChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            mVideoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            Utils.downloader(activity, mVideoUrl, Utils.instaDirPath,  getVidName(mVideoUrl));
                            mVideoUrl = "";

                        } else {
                            mPhotoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            Utils.downloader( activity, mPhotoUrl, Utils.instaDirPath, getImgName(mPhotoUrl));
                            mPhotoUrl = "";
                        }
                    }
                } else {
                    boolean isVideo = insModel.getGetMedia().getShortcode_media().isIs_video();
                    if (isVideo) {
                        mVideoUrl = insModel.getGetMedia().getShortcode_media().getVideo_url();
                        Utils.downloader(activity, mVideoUrl, Utils.instaDirPath, getVidName(mVideoUrl));
                        mVideoUrl = "";
                    } else {
                        mPhotoUrl = insModel.getGetMedia().getShortcode_media().getDisplay_resources()
                                .get(insModel.getGetMedia().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        Utils.downloader(activity, mPhotoUrl, Utils.instaDirPath, getImgName(mPhotoUrl));
                        mPhotoUrl = "";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.dismissLoader();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.dismissLoader();
        }
    };



    private String getImgName(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    private String getVidName(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void destroy(){
        disposableObserver.dispose();
    }
}
