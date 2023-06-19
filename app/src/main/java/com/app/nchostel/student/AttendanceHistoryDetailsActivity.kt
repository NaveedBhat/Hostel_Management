package com.app.nchostel.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.AttendanceHistoryAdapter
import com.app.nchostel.adapters.AttendanceHistoryDetailsAdapter
import com.app.nchostel.models.AttendanceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AttendanceHistoryDetailsActivity : AppCompatActivity() {

    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("attendance")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    //private var mAuth: FirebaseAuth? = null
    var all_btn: AppCompatButton? = null
    var present_btn: AppCompatButton? = null
    var absent_btn: AppCompatButton? = null
    var total_days_tv: TextView? = null
    var total_status: String = "ALL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_history_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

        all_btn = findViewById(R.id.all_btn)
        present_btn = findViewById(R.id.present_btn)
        absent_btn = findViewById(R.id.absent_btn)
        total_days_tv = findViewById(R.id.total_days_tv)

        supportActionBar!!.title=intent.getStringExtra("month_key").toString().replace("_"," ")
        getData(intent.getStringExtra("month_key"))
        all_btn?.setOnClickListener {
            total_status="ALL"
            getData(intent.getStringExtra("month_key"))
        }
        present_btn?.setOnClickListener {

            total_status="present"
            getAbPreData(intent.getStringExtra("month_key"))
        }

        absent_btn?.setOnClickListener {

            total_status="absent"
            getAbPreData(intent.getStringExtra("month_key"))
        }

    }

    private fun getData(month:String?) {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.child(intent.getStringExtra("user_id").toString()).child(month.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<AttendanceModel>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                        val attendanceModel= AttendanceModel(
                            studentList["user_id"].toString(),
                            studentList["name"].toString(),
                            studentList["course"].toString(),
                            studentList["batch"].toString(),
                            studentList["attendance_status"].toString(),
                            studentList["date_and_time"].toString()


                        )

                        userModelList.add(attendanceModel!!)
                    }
                    total_days_tv?.text = "$total_status : ${userModelList.size}"
                    Collections.reverse(userModelList)
                    val adapter = AttendanceHistoryDetailsAdapter(this@AttendanceHistoryDetailsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@AttendanceHistoryDetailsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@AttendanceHistoryDetailsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AttendanceHistoryDetailsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }


    private fun getAbPreData(month:String?) {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.child(intent.getStringExtra("user_id").toString()).child(month.toString()).orderByChild("attendance_status").equalTo(total_status).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<AttendanceModel>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                        val attendanceModel= AttendanceModel(
                            studentList["user_id"].toString(),
                            studentList["name"].toString(),
                            studentList["course"].toString(),
                            studentList["batch"].toString(),
                            studentList["attendance_status"].toString(),
                            studentList["date_and_time"].toString()


                        )

                        userModelList.add(attendanceModel!!)
                    }
                    total_days_tv?.text = "$total_status : ${userModelList.size}"

                    Collections.reverse(userModelList)
                    val adapter = AttendanceHistoryDetailsAdapter(this@AttendanceHistoryDetailsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@AttendanceHistoryDetailsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@AttendanceHistoryDetailsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AttendanceHistoryDetailsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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