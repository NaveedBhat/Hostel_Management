package com.app.nchostel.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.google.firebase.database.FirebaseDatabase

class AdminStudentAttendanceActivity : AppCompatActivity() {
    var name_et: EditText? = null
    var user_id_et: EditText? = null
    var course_et: EditText? = null
    var batch_et: EditText? = null
    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("attendance")
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")

    lateinit var att_switch_: Switch
    lateinit var atten_btn: AppCompatButton

    var att_status:String ="present"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_student_attendance)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Mark Attendance"
        name_et = findViewById(R.id.name_et)
        user_id_et = findViewById(R.id.user_id_et)
        course_et = findViewById(R.id.course_et)
        batch_et = findViewById(R.id.batch_et)
        login_progress = findViewById(R.id.login_progress)
        atten_btn = findViewById(R.id.atten_btn)

        att_switch_ = findViewById(R.id.att_switch_)

        att_switch_.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                att_status="present"
            else
                att_status="absent"
        }

        atten_btn.setOnClickListener {
            var attendanceModel= AttendanceModel(
                user_id_et?.text.toString(),name_et?.text.toString(),course_et?.text.toString(),batch_et?.text.toString(),att_status, AppUtilObject.getCurrentDateAndTime().toString()
            )

            attendanceProcess(attendanceModel)
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
                        Toast.makeText(this@AdminStudentAttendanceActivity, "Attendance Done Successfully", Toast.LENGTH_LONG).show()

                    }


            }.addOnFailureListener({


                //    progressbar GONE
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AdminStudentAttendanceActivity,
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