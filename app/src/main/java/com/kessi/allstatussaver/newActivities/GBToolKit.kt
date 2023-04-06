package com.biyer.statussaver;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast

import com.forms.sti.progresslitieigb.ProgressLoadingIGB
import com.google.android.ads.nativetemplates.TemplateView
import com.kessi.allstatussaver.R
import com.kessi.allstatussaver.utils.AdController
import com.kessi.allstatussaver.utils.SessionManagement


class GBToolKit : AppCompatActivity() {
    private var container: FrameLayout? = null
    private var my_template: TemplateView? = null
    private var session: SessionManagement? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gbtool_kit)
        container = findViewById(R.id.banner_container)
        session= SessionManagement(this)
        my_template= findViewById(R.id.my_template)
        AdController.populateUnifiedNativeAdView(this,my_template)
        AdController.loadInterAd(this)

    }
    fun next(view: View){
        val intent=Intent(applicationContext,StatusSaver::class.java)
        if(session!!.getString("appid").equals("admob")){
            AdController.showAdmob2(this,intent)

        }else{

            AdController.showLoadAppLovinAds1(this,intent)
        }

       // AdController.showAdmob2(this,intent)

    }
    fun start(){
        ProgressLoadingIGB.startLoadingIGB(this){
            message = "Good Morning!" //  Center Message
            srcLottieJson = R.raw.loading_a // Tour Source JSON Lottie
            timer = 10000                   // Time of live for progress.
            hight = 500 // Optional
            width = 500 // Optional

        }
    }
}