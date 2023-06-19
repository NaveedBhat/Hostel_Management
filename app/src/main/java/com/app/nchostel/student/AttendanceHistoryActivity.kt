package com.app.nchostel.student

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
import com.app.nchostel.adapters.MyComplaintsAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.app.nchostel.models.ComplaintsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AttendanceHistoryActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("attendance")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null
    var user_id_i : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_history)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Attendance History"
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)



        if (intent.hasExtra("user_id"))
        {
            user_id_i= intent.getStringExtra("user_id")
            Log.e("ccccuserid",intent.getStringExtra("user_id").toString())
            getDataById(intent.getStringExtra("user_id").toString())
        }else
        {
            user_id_i = mAuth!!.currentUser!!.uid
            getData()
        }
    }


    private fun getData() {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.child(user_id_i.toString()).addValueEventListener(object :
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
                    val adapter = AttendanceHistoryAdapter(this@AttendanceHistoryActivity,userModelList,user_id_i.toString())
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@AttendanceHistoryActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@AttendanceHistoryActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AttendanceHistoryActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun getDataById(userId:String) {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.child(userId).addValueEventListener(object :
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
                    val adapter = AttendanceHistoryAdapter(this@AttendanceHistoryActivity,userModelList,userId)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@AttendanceHistoryActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@AttendanceHistoryActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AttendanceHistoryActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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