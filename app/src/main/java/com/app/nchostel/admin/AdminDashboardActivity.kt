package com.app.nchostel.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.student.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    var add_students_btn: AppCompatButton? = null
    var view_students_btn: AppCompatButton? = null
    var view_complaints_btn: AppCompatButton? = null
    var scanner_btn: AppCompatButton? = null
    var view_attend_btn: AppCompatButton? = null
    var mess_btn: AppCompatButton? = null
    var add_notice_btn: AppCompatButton? = null
    var view_attend_request_btn: AppCompatButton? = null
    lateinit var logout_ib: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        supportActionBar!!.hide()
        mAuth = FirebaseAuth.getInstance()
        add_students_btn =findViewById(R.id.add_students_btn)
        view_students_btn =findViewById(R.id.view_students_btn)
        view_complaints_btn =findViewById(R.id.view_complaints_btn)
        scanner_btn =findViewById(R.id.scanner_btn)
        view_attend_btn =findViewById(R.id.view_attend_btn)
        mess_btn =findViewById(R.id.mess_btn)
        add_notice_btn =findViewById(R.id.add_notice_btn)
        view_attend_request_btn =findViewById(R.id.view_attend_request_btn)

        logout_ib=findViewById(R.id.logout_ib)
        logout_ib.setOnClickListener {

            mAuth?.signOut()
            val preferences = getSharedPreferences(AppUtilObject.PREFS, MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this@AdminDashboardActivity, "Logout Successful ", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@AdminDashboardActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        add_students_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, AddStudentsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        view_students_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, ViewStudentsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        view_complaints_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, ViewComplaintsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        scanner_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, ScannerActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        view_attend_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, ViewAttendanceActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        mess_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, AddMessActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        add_notice_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, AddNoticeActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        view_attend_request_btn?.setOnClickListener {
            val intent = Intent(this@AdminDashboardActivity, ViewAttendanceRequestsActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


    }
}