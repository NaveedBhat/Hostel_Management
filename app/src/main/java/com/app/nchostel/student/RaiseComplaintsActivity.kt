package com.app.nchostel.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.admin.AdminDashboardActivity
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RaiseComplaintsActivity : AppCompatActivity() {

    var title_et: EditText? = null
    var desc_et: EditText? = null

    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.COMPLAINTS_TABLE_KEY)
    var raise_btn: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raise_complaints)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Raise Complaints"
        title_et = findViewById(R.id.title_et)
        desc_et = findViewById(R.id.desc_et)
        login_progress = findViewById(R.id.login_progress)
        raise_btn = findViewById(R.id.raise_btn)


        raise_btn?.setOnClickListener {

            if (title_et!!.getText().length == 0) {
                title_et!!.setError("enter title")
            } else if (desc_et!!.getText().length == 0) {
                desc_et!!.setError("enter description")
            } else {
                    raiseComplaint()
            }

        }
    }

    fun raiseComplaint() {
        login_progress?.visibility = View.VISIBLE


        val id = databaseReference.push().key as String
        val complaintsModel = ComplaintsModel(
            id,
            FirebaseAuth.getInstance().currentUser!!.uid,
            title_et!!.text.toString(),
            desc_et!!.text.toString(),
            "0",
            "",
            AppUtilObject.getCurrentDate() + "," + AppUtilObject.getCurrentTime()

        )

        databaseReference
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(id).setValue(complaintsModel)
            .addOnCompleteListener {
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@RaiseComplaintsActivity,
                    "Complaint raised Successfully ",
                    Toast.LENGTH_LONG
                ).show()
                val intent =
                    Intent(this@RaiseComplaintsActivity, StudentDashboardActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{

                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@RaiseComplaintsActivity,
                    "Something went wrong!",
                    Toast.LENGTH_LONG
                ).show()
            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}