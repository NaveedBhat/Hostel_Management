package com.app.nchostel.admin

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ViewAttendanceDetailsActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="View All Attendance"

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

        supportActionBar!!.title=intent.getStringExtra("month_key").toString().replace("_"," ")
        getData(intent.getStringExtra("month_key"))
    }

    private fun getData(month:String?) {
        progressBar!!.visibility = View.VISIBLE
        databaseReferenceAdmin.child(month.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<String>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>

                    for (tdsss in dataSnapshot.children)
                    {
                        Log.e("seeecccceece",tdsss.key.toString())
                        userModelList.add(tdsss.key.toString()!!)
                    }

                   /* for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                        Log.e("sdawewewewe",key.toString())

                        userModelList.add(key.toString()!!)
                    }*/

                   Collections.reverse(userModelList)


                    val adapter = ViewAttendanceSecondAdapter(this@ViewAttendanceDetailsActivity,userModelList,month.toString())
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewAttendanceDetailsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewAttendanceDetailsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAttendanceDetailsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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