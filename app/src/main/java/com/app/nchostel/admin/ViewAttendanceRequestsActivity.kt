package com.app.nchostel.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.ViewAttendanceAdapter
import com.app.nchostel.adapters.ViewRequestsAttendanceAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.app.nchostel.models.RequestAttendanceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ViewAttendanceRequestsActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_requests")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var accept_all_btn: AppCompatButton? = null

    val userModelListAll = ArrayList<RequestAttendanceModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance_requests)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="View All Attendance Requests"

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)
        accept_all_btn = findViewById(R.id.accept_all_btn)

        getData()

        accept_all_btn?.setOnClickListener {

            for (attenAll in userModelListAll)
            {
                acceptAllAttendUser(attenAll)
            }
        }
    }

    private fun getData() {
        progressBar!!.visibility = View.VISIBLE
        databaseReferenceAdmin.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<RequestAttendanceModel>()
                if (dataSnapshot.exists()) {

                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                          val attendanceModel= RequestAttendanceModel(
                               studentList["id"].toString(),
                               studentList["user_id"].toString(),
                               studentList["name"].toString(),
                               studentList["course"].toString(),
                               studentList["batch"].toString(),
                               studentList["attendance_status"].toString(),
                               studentList["reason"].toString(),
                              studentList["date_and_time"].toString()


                              )

                        Log.e("rereerer",key.toString())
                         userModelList.add(attendanceModel)

                        userModelListAll.add(attendanceModel)
                    }

                    if(userModelList.size>0)
                    {
                        accept_all_btn?.visibility=View.VISIBLE
                    }

                    Collections.reverse(userModelList)
                    val adapter = ViewRequestsAttendanceAdapter(this@ViewAttendanceRequestsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewAttendanceRequestsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewAttendanceRequestsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAttendanceRequestsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    //make attendance on accept

    private fun acceptAllAttendUser(
       requestAttendanceModel: RequestAttendanceModel
    ) {

        var attendanceModel = AttendanceModel(requestAttendanceModel.user_id, requestAttendanceModel.name,
            requestAttendanceModel.course,
            requestAttendanceModel.batch,
            requestAttendanceModel.attendance_status,
            AppUtilObject.getCurrentDateAndTime().toString(),
        )

        attendanceProcess(attendanceModel,requestAttendanceModel)

    }
    fun attendanceProcess(attendanceModel: AttendanceModel,requestAttendanceModel: RequestAttendanceModel)
    {
        var mDatabase = FirebaseDatabase.getInstance()
        var databaseReference = mDatabase.reference.child("attendance")
        var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")
        databaseReference
            .child(attendanceModel.user_id).child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(
                AppUtilObject.getCurrentDayMonthYearForAttendance().toString()).setValue(attendanceModel)
            .addOnCompleteListener {

                //for admin
                databaseReferenceAdmin
                    .child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(
                        AppUtilObject.getCurrentDayMonthYearForAttendance().toString()) .child(attendanceModel.user_id).setValue(attendanceModel)
                    .addOnCompleteListener {
                        Toast.makeText(this@ViewAttendanceRequestsActivity, "All Attendance Accepted Successfully", Toast.LENGTH_LONG).show()
                        //deleteRequest(position = position,holder)
                        deleteRequest(requestAttendanceModel)

                        val intent = Intent(this@ViewAttendanceRequestsActivity, AdminDashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)
                        finish()
                    }


            }.addOnFailureListener({


                //    progressbar GONE
                Toast.makeText(
                    this@ViewAttendanceRequestsActivity,
                    "Attendance Failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()

            })


    }

    private fun deleteRequest(
       requestAttendanceModel: RequestAttendanceModel
    ) {
        val database = FirebaseDatabase.getInstance()
        val mDatabaseRef = database.reference
            .child("attendance_requests")
            .child(requestAttendanceModel.id)
        mDatabaseRef.removeValue().addOnCompleteListener {

        }


    }
}