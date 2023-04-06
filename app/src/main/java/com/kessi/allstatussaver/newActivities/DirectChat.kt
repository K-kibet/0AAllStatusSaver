package com.biyer.statussaver;

import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.nativetemplates.TemplateView
import com.kessi.allstatussaver.R
import com.kessi.allstatussaver.utils.AdController
import com.kessi.allstatussaver.utils.SessionManagement


class DirectChat : AppCompatActivity() {
    private var container: FrameLayout? = null
    private var my_template: TemplateView? = null
    private var session: SessionManagement? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_chat2)
        container = findViewById(R.id.banner_container)
        session=SessionManagement(this)
        my_template= findViewById(R.id.my_template)
        AdController.populateUnifiedNativeAdView(this,my_template)
        AdController.loadInterAd(this)
    }
    fun next(view: View){
        //startActivity(Intent(applicationContext,ToolKitToIn::class.java))
        val intent=Intent(applicationContext,ToolKitToIn::class.java)
        if(session!!.getString("appid").equals("admob")){
            AdController.showAdmob2(this,intent)

        }else{
            //startActivity(intent)
            AdController.showLoadAppLovinAds1(this,intent)
        }
    }
}