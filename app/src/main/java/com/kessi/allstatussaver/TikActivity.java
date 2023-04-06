package com.kessi.allstatussaver;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.Utils;
import com.zhkrb.cloudflare_scrape_webview.CfCallback;
import com.zhkrb.cloudflare_scrape_webview.Cloudflare;

import java.net.HttpCookie;
import java.util.List;


public class TikActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout tikBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String urlTik;


    //    tik download
    WebView tikWeb;
    public static Handler handler;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    boolean isDownloading = false;

    @SuppressLint("HandlerLeak")
    private class handlerListener extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.dismissLoader();
            Utils.displayLoader(TikActivity.this);
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Toast.makeText(TikActivity.this, "Please Wait a moment for start downloading video", Toast.LENGTH_SHORT).show();
            String jsscript = "javascript:(function() { "
                    + "document.getElementById(\"main_page_text\").value ='" + urlTik + "';"
                    + "document.getElementsByTagName('button')[1].click();"
                    + "})();";

            view.evaluateJavascript(jsscript, new ValueCallback() {
                public void onReceiveValue(Object obj) {
                }
            });

            try {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.evaluateJavascript("javascript:document.getElementsByTagName('a')[6].getAttribute('href')", new ValueCallback() {
                            public void onReceiveValue(Object obj) {
                                if (obj.toString() != null && obj.toString().contains("http")) {
                                    Log.e("tik", "receive value" + obj.toString());

                                    handler1.removeCallbacksAndMessages(null);

                                    Utils.dismissLoader();

                                    if (!isDownloading) {
                                        isDownloading = true;
                                        String timeStamp = String.valueOf(System.currentTimeMillis());
                                        String file = "Tiktok" + "_" + timeStamp;
                                        String ext = "mp4";
                                        String fileName = file + "." + ext;

                                        Utils.downloader(TikActivity.this, obj.toString().replace("\"", ""), Utils.tiktokDirPath, fileName);
                                    }
                                }
                            }
                        });

                        handler1.postDelayed(this, 2000);

                    }
                }, 2000);
            } catch (Exception e) {
                Utils.dismissLoader();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tik);


        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(TikActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.tiktok_step01))
                .into(help1);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step02)
                .into(help2);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step03)
                .into(help3);

        Glide.with(TikActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);


        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDownloading = false;
                downloadClick();
            }
        });


        tikBtn = findViewById(R.id.tikBtn);
        tikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTok();
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        Tik download

        tikWeb = findViewById(R.id.webViewscan);

        initHandler();


        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(TikActivity.this);
            AdManager.loadBannerAd(TikActivity.this, adContainer);
            AdManager.adptiveBannerAd(TikActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(TikActivity.this);
            AdManager.maxBanner(TikActivity.this, adContainer);
            AdManager.maxBannerAdaptive(TikActivity.this, adaptiveAdContainer);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tikWeb.clearCache(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void downloadClick() {
        if (Utils.isNetworkAvailable(TikActivity.this)) {
            if (linkEdt.getText().toString().trim().length() == 0) {
                Toast.makeText(TikActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else {
                urlTik = linkEdt.getText().toString();
                linkEdt.getText().clear();
                if (urlTik.contains("tiktok")) {
                    Log.e("tiktok", urlTik);
                    init();
                } else {
                    Toast.makeText(TikActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                }

                //ads
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(TikActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(TikActivity.this, null);
                }

            }
        } else {
            Toast.makeText(TikActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    void init() {
        if (Build.VERSION.SDK_INT >= 24) {
            onstart();
            tikWeb.clearFormData();
            tikWeb.getSettings().setSaveFormData(true);
            tikWeb.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            tikWeb.setWebViewClient(new MyBrowser());
            tikWeb.getSettings().setAppCacheMaxSize(5242880);
            tikWeb.getSettings().setAllowFileAccess(true);
            tikWeb.getSettings().setAppCacheEnabled(true);
            tikWeb.getSettings().setJavaScriptEnabled(true);
            tikWeb.getSettings().setDefaultTextEncodingName("UTF-8");
            tikWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            tikWeb.getSettings().setDatabaseEnabled(true);
            tikWeb.getSettings().setBuiltInZoomControls(false);
            tikWeb.getSettings().setSupportZoom(true);
            tikWeb.getSettings().setUseWideViewPort(true);
            tikWeb.getSettings().setDomStorageEnabled(true);
            tikWeb.getSettings().setAllowFileAccess(true);
            tikWeb.getSettings().setLoadWithOverviewMode(true);
            tikWeb.getSettings().setLoadsImagesAutomatically(true);
            tikWeb.getSettings().setBlockNetworkImage(false);
            tikWeb.getSettings().setBlockNetworkLoads(false);
            tikWeb.getSettings().setLoadWithOverviewMode(true);


            tikWeb.setWebChromeClient(new WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });
            tikWeb.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {

                    Utils.dismissLoader();
                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String file = "Tiktok" + "_" + timeStamp;
                    String ext = "mp4";
                    String fileName = file + "." + ext;

                    Utils.downloader(TikActivity.this, url.replace("\"", ""), Utils.tiktokDirPath, fileName);
                }
            });

            tikWeb.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        Utils.dismissLoader();
                    }
                }
            });

            Cloudflare cf = new Cloudflare(TikActivity.this, "https://tiktokdownload.online/");
            cf.setUser_agent(tikWeb.getSettings().getUserAgentString());
            cf.setCfCallback(new CfCallback() {
                @Override
                public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                    tikWeb.loadUrl(newUrl);
                }

                @Override
                public void onFail(int code, String msg) {
                    tikWeb.loadUrl("https://tiktokdownload.online/");
                }
            });
            cf.getCookies();
        } else {
            onstart();
            tikWeb.clearFormData();
            tikWeb.getSettings().setSaveFormData(true);
            tikWeb.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            tikWeb.setWebViewClient(new MyBrowser());
            tikWeb.getSettings().setAppCacheMaxSize(5242880);
            tikWeb.getSettings().setAllowFileAccess(true);
            tikWeb.getSettings().setAppCacheEnabled(true);
            tikWeb.getSettings().setJavaScriptEnabled(true);
            tikWeb.getSettings().setDefaultTextEncodingName("UTF-8");
            tikWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            tikWeb.getSettings().setDatabaseEnabled(true);
            tikWeb.getSettings().setBuiltInZoomControls(false);
            tikWeb.getSettings().setSupportZoom(false);
            tikWeb.getSettings().setUseWideViewPort(true);
            tikWeb.getSettings().setDomStorageEnabled(true);
            tikWeb.getSettings().setAllowFileAccess(true);
            tikWeb.getSettings().setLoadWithOverviewMode(true);
            tikWeb.getSettings().setLoadsImagesAutomatically(true);
            tikWeb.getSettings().setBlockNetworkImage(false);
            tikWeb.getSettings().setBlockNetworkLoads(false);
            tikWeb.getSettings().setLoadWithOverviewMode(true);
            tikWeb.setWebChromeClient(new WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });

            tikWeb.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Utils.dismissLoader();
                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String file = "Tiktok" + "_" + timeStamp;
                    String ext = "mp4";
                    String fileName = file + "." + ext;

                    Utils.downloader(TikActivity.this, url.replace("\"", ""), Utils.tiktokDirPath, fileName);
                }
            });

            tikWeb.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        Utils.dismissLoader();
                    }
                }
            });

            Cloudflare cf = new Cloudflare(TikActivity.this, "https://tiktokdownload.online/");
            cf.setUser_agent(tikWeb.getSettings().getUserAgentString());
            cf.setCfCallback(new CfCallback() {
                @Override
                public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                    tikWeb.loadUrl(newUrl);
                }

                @Override
                public void onFail(int code, String msg) {
                    tikWeb.loadUrl("https://tiktokdownload.online/");
                }
            });
            cf.getCookies();
        }

    }

    private void openTok() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            this.startActivity(i);
        } catch (Exception var4) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.zhiliaoapp.musically")));
        }

    }


    public void onstart() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.EXPAND_STATUS_BAR"}, 123);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && Build.VERSION.SDK_INT >= 21) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (tikWeb.canGoBack()) {
                    tikWeb.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    @Override
    protected void onPause() {
        super.onPause();
        tikWeb.clearCache(true);
    }

    @Override
    protected void onStop() {
        tikWeb.clearCache(true);
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void initHandler() {
        handler = new handlerListener();
    }


}
