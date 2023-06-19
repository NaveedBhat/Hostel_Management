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
import com.app.nchostel.adapters.StudentListAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ViewStudentsActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth? = null

    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.USERS_TABLE_KEY)

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_students)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="View Students"

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

        getData()
    }

    private fun getData() {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.orderByChild("user_type").equalTo("student").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModelList = ArrayList<UserModel>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys){
                        val studentList = td[key] as HashMap<*, *>

                        val userModel= UserModel(
                            studentList["user_id"].toString(),
                            studentList["user_name"].toString(),
                            studentList["user_email"].toString(),
                            studentList["user_password"].toString(),
                            studentList["user_type"].toString(),
                            studentList["user_image"].toString(),
                            studentList["course"].toString(),
                            studentList["batch"].toString(),
                            studentList["enrollment_no"].toString(),
                            studentList["personal_contact"].toString(),
                            studentList["gaurdian_contact"].toString(),
                            studentList["address"].toString(),
                            studentList["gender"].toString(),
                            studentList["room_number"].toString(),
                            studentList["date_and_time"].toString(),

                        )

                        userModelList.add(userModel!!)
                    }

                    Collections.reverse(userModelList)
                    val adapter = StudentListAdapter(this@ViewStudentsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewStudentsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewStudentsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewStudentsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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