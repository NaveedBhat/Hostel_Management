package com.app.nchostel.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.AttendanceHistoryAdapter
import com.app.nchostel.adapters.ViewAttendanceAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ViewAttendanceActivity : AppCompatActivity() {

    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="View All Attendance"

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

        getData()


    }

    private fun getData() {
        progressBar!!.visibility = View.VISIBLE
        databaseReferenceAdmin.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<String>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                        /*  val attendanceModel= AttendanceModel(
                               studentList["user_id"].toString(),
                               studentList["attendance_status"].toString(),
                              studentList["date_and_time"].toString()


                              )*/

                        Log.e("sdawewewewe",key.toString())

                        userModelList.add(key.toString()!!)
                    }

                   Collections.reverse(userModelList)
                    val adapter = ViewAttendanceAdapter(this@ViewAttendanceActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewAttendanceActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewAttendanceActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAttendanceActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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
}