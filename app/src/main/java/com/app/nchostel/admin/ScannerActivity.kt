package com.app.nchostel.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject


class ScannerActivity : AppCompatActivity() {

    var scan_btn:AppCompatButton?=null
    var content_tv:TextView?=null


    var login_progress: ProgressBar? = null
    var mark_atten_btn: AppCompatButton? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("attendance")
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")

    lateinit var att_switch_: Switch

    var att_status:String ="present"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Scanner"

        scan_btn=findViewById(R.id.scan_btn)
        content_tv=findViewById(R.id.content_tv)
        login_progress = findViewById(R.id.login_progress)
        mark_atten_btn = findViewById(R.id.mark_atten_btn)

        att_switch_ = findViewById(R.id.att_switch_)

        att_switch_.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                att_status="present"
            else
                att_status="absent"
        }

        scan_btn?.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setPrompt("Scan Student QR Code")
            intentIntegrator.setOrientationLocked(true)

            intentIntegrator.initiateScan()
        }

        mark_atten_btn?.setOnClickListener {
            val intent = Intent(this@ScannerActivity, AdminStudentAttendanceActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Scanning Cancelled", Toast.LENGTH_SHORT).show()
            } else {

                var mainObject = JSONObject(intentResult.contents)

                var dataObject=mainObject.getJSONObject("data")
                var user_id= dataObject.getString("user_id")
                var name= dataObject.getString("name")
                var batch= dataObject.getString("batch")
                var course= dataObject.getString("course")

                Log.e("attsta",att_status)
                var attendanceModel=AttendanceModel(
                    user_id,name,course,batch,att_status,AppUtilObject.getCurrentDateAndTime().toString()
                )

                attendanceProcess(attendanceModel)

             }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun attendanceProcess(attendanceModel: AttendanceModel)
    {
        login_progress!!.setVisibility(View.VISIBLE)
        databaseReference
            .child(attendanceModel.user_id).child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(AppUtilObject.getCurrentDayMonthYearForAttendance().toString()).setValue(attendanceModel)
            .addOnCompleteListener {

                //for admin
                databaseReferenceAdmin
                   .child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(AppUtilObject.getCurrentDayMonthYearForAttendance().toString()) .child(attendanceModel.user_id).setValue(attendanceModel)
                    .addOnCompleteListener {
                        login_progress!!.setVisibility(View.GONE)
                        Toast.makeText(this@ScannerActivity, "Attendance Done Successfully", Toast.LENGTH_LONG).show()
                        content_tv?.setText("User Id : ${attendanceModel.user_id} \nStatus : ${attendanceModel.attendance_status}")

                    }


            }.addOnFailureListener({


                //    progressbar GONE
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@ScannerActivity,
                    "Attendance Failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()

            })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}