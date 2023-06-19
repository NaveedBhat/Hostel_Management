package com.app.nchostel.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.app.nchostel.models.RequestAttendanceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.util.HashMap

class RequestAttendanceActivity : AppCompatActivity() {

    var reason_et: EditText? = null
    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("attendance_requests")

    var userdatabaseReference = mDatabase.reference.child(AppUtilObject.USERS_TABLE_KEY)

    lateinit var att_switch_: Switch
    lateinit var atten_btn: AppCompatButton
    var att_status:String ="present"
    var name_str:String =""
    var batch_str:String =""
    var course_str:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_attendance)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Attendance Request"
        reason_et = findViewById(R.id.reason_et)

        login_progress = findViewById(R.id.login_progress)
        atten_btn = findViewById(R.id.atten_btn)

        att_switch_ = findViewById(R.id.att_switch_)

        att_switch_.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                att_status="present"
            else
                att_status="absent"
        }

        val preferences = getSharedPreferences(AppUtilObject.PREFS, MODE_PRIVATE)
        var user_id = preferences.getString(AppUtilObject.USER_ID,"");


        getMyProfile(user_id.toString())
        atten_btn.setOnClickListener {

            if (reason_et?.text.toString().isEmpty())
            {
                reason_et?.error= "Write your Reason"
            }else {
                val id = databaseReference.push().key as String
                var attendanceModel = RequestAttendanceModel(id,
                    user_id.toString(),
                    name_str,
                    course_str,
                    batch_str,
                    att_status,
                    reason_et?.text.toString(),
                    AppUtilObject.getCurrentDateAndTime().toString()
                )

                attendanceProcess(attendanceModel)
            }
        }
    }

    fun attendanceProcess(attendanceModel: RequestAttendanceModel)
    {
        login_progress!!.setVisibility(View.VISIBLE)
        databaseReference
            .child(attendanceModel.id).setValue(attendanceModel)
            .addOnCompleteListener {
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(this@RequestAttendanceActivity, "Request sent Successfully", Toast.LENGTH_LONG).show()



            }.addOnFailureListener({


                //    progressbar GONE
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@RequestAttendanceActivity,
                    "Request Failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()

            })


    }


    fun getMyProfile(user_id:String)
    {
        login_progress?.visibility= View.VISIBLE
        userdatabaseReference.child(user_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {

                        login_progress?.visibility= View.GONE

                        val td = dataSnapshot.value as HashMap<*, *>

                        name_str =td["user_name"].toString()
                        course_str =td["course"].toString()
                        batch_str =td["batch"].toString()




                    } else {
                        login_progress?.visibility= View.GONE
                        Toast.makeText(this@RequestAttendanceActivity, "Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {  login_progress?.visibility= View.GONE}
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}