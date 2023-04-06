package com.kessi.allstatussaver

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kessi.allstatussaver.utils.SessionManagement
import com.kessi.allstatussaver.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.biyer.statussaver.GBToolKit
import com.biyer.statussaver.ToolKitToIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {
    var spicon: ImageView? = null
    var session: SessionManagement? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(SharedPrefs.getAppNightDayMode(this))
        spicon = findViewById(R.id.spicon)
        session = SessionManagement(this)
        Glide.with(this@SplashActivity)
            .load(R.drawable.sp_icon)
            .into(spicon!!)

                val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("ids")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<String, Any>?
                session!!.setString("map",map.toString())
                val myVeryOwnIterator: Iterator<*> = map!!.keys.iterator()
                while (myVeryOwnIterator.hasNext()) {
                    val key = myVeryOwnIterator.next() as String
                    val value = map!![key] as String?
                    session!!.setString(key,value)
//                    Toast.makeText(applicationContext, "Key: $key Value: $value", Toast.LENGTH_LONG)
//                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        gotoNext()

    }

    fun gotoNext() {
        Handler().postDelayed({ //HomeActivity
//                Intent i = new Intent(SplashActivity.this, GBToolKit.class);
//                startActivity(i);
//                finish();
            val isloggedin = session!!.getString("isloggedin")
            if (null == isloggedin) {
                val i = Intent(this@SplashActivity, GBToolKit::class.java)
                startActivity(i)
            } else {
                session!!.setString("isloggedin", "yes")
                val i = Intent(this@SplashActivity, ToolKitToIn::class.java)
                startActivity(i)
            }
            finish()
        }, (2 * 1000).toLong())
    } //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == 2296) {
    //            if (SDK_INT >= Build.VERSION_CODES.R) {
    //                if (Environment.isExternalStorageManager()) {
    //                    // perform action when allow permission success
    //                    gotoNext();
    //                } else {
    //                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
    //                }
    //            }
    //        }
    //    }
}