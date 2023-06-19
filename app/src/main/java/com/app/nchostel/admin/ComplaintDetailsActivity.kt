package com.app.nchostel.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.UserModel
import com.app.nchostel.student.StudentDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ComplaintDetailsActivity : AppCompatActivity() {


    var title_tv: TextView? = null
    var date_tv: TextView? = null
    var desc_tv: TextView? = null
    var reply_by_admin_tv: TextView? = null
    var reply_et: EditText? = null


    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.COMPLAINTS_TABLE_KEY)
    var view_profile_btn: AppCompatButton? = null
    var do_reply_btn: AppCompatButton? = null

    lateinit var r_ll:LinearLayout
    lateinit var r_admin_ll:LinearLayout

    lateinit var complaintsModel:ComplaintsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Complaint Details"

        title_tv = findViewById(R.id.title_tv)
        date_tv = findViewById(R.id.date_tv)
        desc_tv = findViewById(R.id.desc_tv)
        reply_by_admin_tv = findViewById(R.id.reply_by_admin_tv)
        reply_et = findViewById(R.id.reply_et)
        login_progress = findViewById(R.id.login_progress)
        view_profile_btn = findViewById(R.id.view_profile_btn)
        r_ll = findViewById(R.id.r_ll)
        r_admin_ll = findViewById(R.id.r_admin_ll)
        do_reply_btn = findViewById(R.id.do_reply_btn)

        complaintsModel = intent.getSerializableExtra("data") as ComplaintsModel

        title_tv!!.text=complaintsModel.title
        date_tv!!.text=complaintsModel.date_and_time
        desc_tv!!.text=complaintsModel.description

        view_profile_btn?.setOnClickListener {

            val intent = Intent(this, ProfileDetailsActivity::class.java)
            intent.putExtra("user_id",complaintsModel.user_id)
            intent.putExtra("my_intent",3)
            startActivity(intent)
        }



        if (intent.getIntExtra("my_intent",0)==2)
        {

            if (complaintsModel.reply_status.equals("1"))
            {  //hide

                r_admin_ll.visibility=View.VISIBLE
                r_ll.visibility=View.GONE
                view_profile_btn?.visibility=View.GONE
                reply_by_admin_tv?.text = complaintsModel.reply_text

            }else{
                r_admin_ll.visibility=View.GONE
                r_ll.visibility=View.GONE
                view_profile_btn?.visibility=View.GONE
            }



        }else{
            if (complaintsModel.reply_status.equals("1"))
            {
                r_ll.visibility=View.GONE
                r_admin_ll.visibility=View.VISIBLE
                reply_by_admin_tv?.text = complaintsModel.reply_text
            }else {
                r_admin_ll.visibility = View.GONE
                r_ll.visibility = View.VISIBLE
                view_profile_btn?.visibility = View.VISIBLE
            }
        }

        do_reply_btn?.setOnClickListener {

            if (reply_et!!.getText().length == 0) {
                reply_et!!.setError("enter your text")
            }
            else
            {
               replyComplaint()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun replyComplaint() {
        login_progress?.visibility = View.VISIBLE

        databaseReference
            .child(complaintsModel!!.user_id).child(complaintsModel!!.id).child("reply_text").setValue(reply_et!!.text.toString())
            .addOnCompleteListener {
                login_progress!!.setVisibility(View.GONE)

                databaseReference
                    .child(complaintsModel!!.user_id).child(complaintsModel!!.id).child("reply_status").setValue("1")
                Toast.makeText(
                    this@ComplaintDetailsActivity,
                    "Complaint replied Successfully ",
                    Toast.LENGTH_LONG
                ).show()
                val intent =
                    Intent(this@ComplaintDetailsActivity, ViewComplaintsActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{

                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@ComplaintDetailsActivity,
                    "Something went wrong!",
                    Toast.LENGTH_LONG
                ).show()
            }


    }
}