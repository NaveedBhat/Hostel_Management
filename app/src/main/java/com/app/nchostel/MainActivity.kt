package com.app.nchostel

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.app.nchostel.admin.AdminDashboardActivity

import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.student.LoginActivity
import com.app.nchostel.student.StudentDashboardActivity

class MainActivity : AppCompatActivity() {
    var sharedPref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        sharedPref = getSharedPreferences(AppUtilObject.PREFS, 0)

        var is_login = sharedPref?.getBoolean(AppUtilObject.IS_LOGIN,false)
        var user_type = sharedPref?.getString(AppUtilObject.USER_Type,"")
        Handler().postDelayed({

            if (is_login == false) {
                AppUtilObject.startActivity(this@MainActivity, LoginActivity::class.java)
                finish()
            } else {
                if(user_type=="student") {
                    AppUtilObject.startActivity(
                        this@MainActivity,
                        StudentDashboardActivity::class.java
                    )
                    finish()
                }else{
                    AppUtilObject.startActivity(
                        this@MainActivity,
                        AdminDashboardActivity::class.java
                    )
                    finish()
                }
            }
        }, 3000)

    }
}