package com.app.nchostel.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.MyComplaintsAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.MessModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MessScheduleActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("mess")

    var day_tv: TextView? = null
    var lunch_desc_tv: TextView? = null
    var dinner_desc_tv: TextView? = null
    var progressBar: ProgressBar? = null
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess_schedule)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Mess Schedule"
          progressBar = findViewById(R.id.login_progress)
         day_tv = findViewById(R.id.day_tv)
         lunch_desc_tv = findViewById(R.id.lunch_desc_tv)
         dinner_desc_tv = findViewById(R.id.dinner_desc_tv)

         getData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getData() {
        progressBar!!.visibility = View.VISIBLE
        databaseReference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                 if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());
                     progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                     for (key in td.keys){

                         Log.e("awewewd",key.toString());


                         //val studentList = td[key] as HashMap<*, *>

                       /* val messModel= MessModel(

                            studentList["day_title"].toString(),
                            studentList["lunch_desc"].toString(),
                            studentList["dinner_desc"].toString(),

                            )*/

                        day_tv?.text =  "Day : "+dataSnapshot.child("day_title").getValue().toString()
                        lunch_desc_tv?.text = dataSnapshot.child("lunch_desc").getValue().toString()
                        dinner_desc_tv?.text = dataSnapshot.child("dinner_desc").getValue().toString()


                }


                } else {
                    Toast.makeText(this@MessScheduleActivity, "Not Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessScheduleActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }
}