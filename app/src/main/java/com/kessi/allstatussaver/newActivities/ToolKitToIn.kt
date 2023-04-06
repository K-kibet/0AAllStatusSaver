package com.biyer.statussaver;

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.google.android.ads.nativetemplates.TemplateView
import com.kessi.allstatussaver.HomeActivity
import com.kessi.allstatussaver.R
import com.kessi.allstatussaver.utils.AdController
import com.kessi.allstatussaver.utils.SessionManagement


class ToolKitToIn : AppCompatActivity() {
    private var container: FrameLayout? = null
    private var exitApp_Dialog: Dialog? = null
    var my_template: TemplateView? =null
    private var rating: Bundle? = null
    var session: SessionManagement?=null
    private var nativeAd_FL_1: FrameLayout? = null
    private var nativeAd_FL_2: FrameLayout? = null
    private var al_nativeAd_FL_1: FrameLayout? = null
    private var ld_nativeAd_FL_1: FrameLayout? = null
    private var ea_nativeAd_FL_1: FrameLayout? = null
    private var iw_nativeAd_FL: FrameLayout? = null
    private var iwbs_nativeAd_FL: FrameLayout? = null
    private var about_nativeAd_FL: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_kit_to_in)
        session=SessionManagement(this);
        container = findViewById(R.id.banner_container)
        my_template= findViewById(R.id.my_template)
        AdController.populateUnifiedNativeAdView(this,my_template)
        AdController.loadInterAd(this)
    }
    fun next(view: View){
        val intent=Intent(applicationContext, HomeActivity::class.java)
        if(session!!.getString("appid").equals("admob")){
            AdController.showAdmob2(this,intent)

        }else{
            AdController.showLoadAppLovinAds1(this,intent)
        }
        session!!.setString("isloggedin", "yes")
       // AdController.showAdmob2(this,intent)
        //startActivity(Intent(applicationContext,MainActivity::class.java))
    }

}