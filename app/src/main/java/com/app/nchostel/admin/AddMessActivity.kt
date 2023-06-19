package com.app.nchostel.admin

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
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.MessModel
import com.app.nchostel.student.MessScheduleActivity
import com.app.nchostel.student.StudentDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddMessActivity : AppCompatActivity() {
    var title_et: EditText? = null
    var lunch_et: EditText? = null
    var dinner_et: EditText? = null

    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("mess")
    var add_mess_btn: AppCompatButton? = null
    var mess_btn: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mess)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Add Mess Schedule"

        title_et = findViewById(R.id.title_et)
        lunch_et =findViewById(R.id.lunch_et)
        dinner_et =findViewById(R.id.dinner_et)
        login_progress = findViewById(R.id.login_progress)
        add_mess_btn = findViewById(R.id.add_mess_btn)
        mess_btn = findViewById(R.id.mess_btn)


        add_mess_btn?.setOnClickListener {

            if (title_et!!.getText().length == 0) {
                title_et!!.setError("enter Day")
            } else if (lunch_et!!.getText().length == 0) {
                lunch_et!!.setError("enter lunch description")
            }else if (dinner_et!!.getText().length == 0) {
                dinner_et!!.setError("enter dinner description")
            } else {
                addMess()
            }

        }

        mess_btn?.setOnClickListener {
            val intent = Intent(this@AddMessActivity, MessScheduleActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    fun addMess() {
        login_progress?.visibility = View.VISIBLE
         //val id = databaseReference.push().key as String
        val messModel = MessModel(

             title_et!!.text.toString(),
            lunch_et!!.text.toString(),
            dinner_et!!.text.toString(),


        )

        databaseReference.setValue(messModel)
            .addOnCompleteListener {
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AddMessActivity,
                    "Mess Scheduled Successfully ",
                    Toast.LENGTH_LONG
                ).show()
               /* val intent =
                    Intent(this@AddMessActivity, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()*/
            }.addOnFailureListener{

                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AddMessActivity,
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