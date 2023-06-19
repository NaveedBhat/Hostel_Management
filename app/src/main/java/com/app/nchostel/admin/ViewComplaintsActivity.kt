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
import com.app.nchostel.adapters.MyComplaintsAdapter
import com.app.nchostel.adapters.ViewComplaintsAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ViewComplaintsActivity : AppCompatActivity() {

    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.COMPLAINTS_TABLE_KEY)

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    private val mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_complaints)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="View Complaints"

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

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
                val userModelList = ArrayList<ComplaintsModel>()
                if (dataSnapshot.exists()) {
                    Log.e("saawewe",dataSnapshot.getValue().toString());

                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE


                    for (ds in dataSnapshot.children)
                    {
                        databaseReference.child(ds.key.toString()).addValueEventListener(object :
                            ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {

                                if (snapshot.exists())
                                {
                                    val td = snapshot.value as HashMap<*, *>
                                    for (key in td.keys){
                                        val studentList = td[key] as HashMap<*, *>

                                        Log.e("keyss","ssa "+td[key].toString())
                                        val complaintsModel= ComplaintsModel(
                                            studentList["id"].toString(),
                                            studentList["user_id"].toString(),
                                            studentList["title"].toString(),
                                            studentList["description"].toString(),
                                            studentList["reply_status"].toString(),
                                            studentList["reply_text"].toString(),
                                            studentList["date_and_time"].toString(),

                                            )

                                        userModelList.add(complaintsModel)
                                    }

                                    Collections.reverse(userModelList)
                                    val adapter = ViewComplaintsAdapter(this@ViewComplaintsActivity,userModelList)
                                    recyclerView!!.setHasFixedSize(true)
                                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewComplaintsActivity)
                                    recyclerView!!.adapter = adapter
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        }


                        )
                    }




                } else {
                    Toast.makeText(this@ViewComplaintsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewComplaintsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }
}