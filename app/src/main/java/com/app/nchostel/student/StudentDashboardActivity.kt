package com.app.nchostel.student

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.app.nchostel.R
import com.app.nchostel.admin.AddMessActivity
import com.app.nchostel.admin.ProfileDetailsActivity
import com.app.nchostel.apputil.AppUtilObject
import com.google.firebase.auth.FirebaseAuth

class StudentDashboardActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    var sharedPref: SharedPreferences? = null
    lateinit var logout_ib:ImageButton
    lateinit var my_profile_btn:AppCompatButton
    lateinit var raise_complaints_btn:AppCompatButton
    lateinit var my_complaints_btn:AppCompatButton
    lateinit var qr_code_btn:AppCompatButton
    lateinit var atten_btn:AppCompatButton
    lateinit var mess_btn:AppCompatButton
    lateinit var view_notice_btn:AppCompatButton
    lateinit var about_us_btn:AppCompatButton
    lateinit var request_attend_btn:AppCompatButton
    lateinit var emerg_contact_btn:AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        supportActionBar!!.hide()
        sharedPref = getSharedPreferences(AppUtilObject.PREFS, 0)

        mAuth = FirebaseAuth.getInstance()
        my_profile_btn=findViewById(R.id.my_profile_btn)
        raise_complaints_btn=findViewById(R.id.raise_complaints_btn)
        my_complaints_btn=findViewById(R.id.my_complaints_btn)
        qr_code_btn=findViewById(R.id.qr_code_btn)
        atten_btn=findViewById(R.id.atten_btn)
        mess_btn=findViewById(R.id.mess_btn)
        view_notice_btn=findViewById(R.id.view_notice_btn)
        about_us_btn=findViewById(R.id.about_us_btn)
        request_attend_btn=findViewById(R.id.request_attend_btn)
        emerg_contact_btn=findViewById(R.id.emerg_contact_btn)
        logout_ib=findViewById(R.id.logout_ib)
        logout_ib.setOnClickListener {

            mAuth?.signOut()
            val preferences = getSharedPreferences(AppUtilObject.PREFS, MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this@StudentDashboardActivity, "Logout Successful ", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@StudentDashboardActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        my_profile_btn.setOnClickListener {
            val intent = Intent(this, ProfileDetailsActivity::class.java)
             intent.putExtra("my_intent",2)
            startActivity(intent)
        }
        raise_complaints_btn.setOnClickListener {
            val intent = Intent(this, RaiseComplaintsActivity::class.java)
            startActivity(intent)
        }

        my_complaints_btn.setOnClickListener {
            val intent = Intent(this, MyComplaintsActivity::class.java)
            startActivity(intent)
        }
        qr_code_btn.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity(intent)
        }
        atten_btn.setOnClickListener {
            val intent = Intent(this, AttendanceHistoryActivity::class.java)
            startActivity(intent)
        }

        mess_btn?.setOnClickListener {
            val intent = Intent(this@StudentDashboardActivity, MessScheduleActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        view_notice_btn?.setOnClickListener {
            val intent = Intent(this@StudentDashboardActivity, ViewNoticeActivity::class.java)
            intent.putExtra("intent_from",1)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        about_us_btn?.setOnClickListener {
            val intent = Intent(this@StudentDashboardActivity, AboutUsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        request_attend_btn?.setOnClickListener {
            val intent = Intent(this@StudentDashboardActivity, RequestAttendanceActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        emerg_contact_btn?.setOnClickListener {
            val intent = Intent(this@StudentDashboardActivity, EmergencyContatcsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}