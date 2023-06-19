package com.app.nchostel.admin

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.ViewAttendanceDetailsAdapter
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import java.util.*
import kotlin.collections.ArrayList


class ViewAttendanceDayDetailsActivity : AppCompatActivity() {
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var all_btn: AppCompatButton? = null
    var present_btn: AppCompatButton? = null
    var absent_btn: AppCompatButton? = null
    var total_days_tv: TextView? = null
    var create_btn: AppCompatButton? = null
    var total_status: String = "ALL"
    var dateString: String = "ALL"

    private val PERMISSION_REQUEST_CODE = 200

    var pageHeight = 1120
    var pagewidth = 792

    // creating a bitmap variable
    // for storing our images
    var bmp: Bitmap? = null
    var scaledbmp: Bitmap? = null

    lateinit var pdf_text: ArrayList<AttendanceModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance_day_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        all_btn = findViewById(R.id.all_btn)
        present_btn = findViewById(R.id.present_btn)
        absent_btn = findViewById(R.id.absent_btn)
        total_days_tv = findViewById(R.id.total_days_tv)
        create_btn = findViewById(R.id.create_btn)

        progressBar = findViewById(R.id.signUp_progress)
        recyclerView = findViewById(R.id.recyclerView)

        supportActionBar!!.title=intent.getStringExtra("day_key").toString().replace("_"," ")
        getData(intent.getStringExtra("month_key"),intent.getStringExtra("day_key"))

        dateString =intent.getStringExtra("day_key").toString().replace("_"," ")

        all_btn?.setOnClickListener {
            total_status="ALL"
            getData(intent.getStringExtra("month_key"),intent.getStringExtra("day_key"))

        }
        present_btn?.setOnClickListener {

            total_status="present"
            getAbsentPresentData(intent.getStringExtra("month_key"),intent.getStringExtra("day_key"))

        }

        absent_btn?.setOnClickListener {

            total_status="absent"
            getAbsentPresentData(intent.getStringExtra("month_key"),intent.getStringExtra("day_key"))

        }

        pdf_text = ArrayList<AttendanceModel>()

        create_btn?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {


                AppUtilObject.createPdf( dateS = dateString,pdf_text,this@ViewAttendanceDayDetailsActivity)

            } else {
                requestAllPermission()
            }
        }
    }

    private fun getData(month:String?, day:String?) {
        progressBar!!.visibility = View.VISIBLE
        databaseReferenceAdmin.child(month.toString()).child(day.toString()).addValueEventListener(object :
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


                        pdf_text.add(attendanceModel)
                    }


                    total_days_tv?.text = "$total_status : ${userModelList.size}"

                    Collections.reverse(userModelList)
                    val adapter = ViewAttendanceDetailsAdapter(this@ViewAttendanceDayDetailsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewAttendanceDayDetailsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewAttendanceDayDetailsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAttendanceDayDetailsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun getAbsentPresentData(month:String?, day:String?) {
        progressBar!!.visibility = View.VISIBLE
        databaseReferenceAdmin.child(month.toString()).child(day.toString()).orderByChild("attendance_status").equalTo(total_status).addValueEventListener(object :
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
                    val adapter = ViewAttendanceDetailsAdapter(this@ViewAttendanceDayDetailsActivity,userModelList)
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewAttendanceDayDetailsActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewAttendanceDayDetailsActivity, "No List Found.", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAttendanceDayDetailsActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
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

    ///////////////////////////pdf ///////////////


    private fun requestAllPermission() {
        ActivityCompat.requestPermissions(
            this@ViewAttendanceDayDetailsActivity, arrayOf(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            ), REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@ViewAttendanceDayDetailsActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


