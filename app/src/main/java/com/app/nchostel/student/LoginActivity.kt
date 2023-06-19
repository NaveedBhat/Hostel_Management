package com.app.nchostel.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.admin.AdminDashboardActivity
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    private var email_et: EditText? = null
    var password_et:EditText? = null
      var login_btn: AppCompatButton? = null

    var login_progress: ProgressBar? = null


    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.USERS_TABLE_KEY)

     private val isShow = false
    var radio_group: RadioGroup? = null
    var user_select_type:String="student"
    lateinit var student_rb: RadioButton
    lateinit var admin_rb: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        email_et = findViewById(R.id.email_et)
        password_et = findViewById(R.id.password_et)
        login_progress = findViewById(R.id.login_progress)
        radio_group = findViewById(R.id.radio_group)
        student_rb = findViewById(R.id.student_rb)
        admin_rb = findViewById(R.id.admin_rb)
        login_btn = findViewById(R.id.login_btn)
        mAuth = FirebaseAuth.getInstance()

        FirebaseAuth.getInstance().signOut()

        radio_group?.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                student_rb -> {
                    user_select_type="student"
                }
                admin_rb -> {
                    user_select_type="admin"
                }
            }
        }


        login_btn?.setOnClickListener {

            //Toast.makeText(this, user_select_type, Toast.LENGTH_SHORT).show()
          openHome()

        }

    }


    fun openHome() {
        login_progress!!.visibility = View.VISIBLE
        mAuth!!.signInWithEmailAndPassword(
            email_et!!.text.toString(),
            password_et!!.text.toString()
        ).addOnCompleteListener { task ->
            login_progress!!.visibility = View.GONE
            if (task.isSuccessful) {


                    databaseReference.child(mAuth?.currentUser!!.uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    val td = dataSnapshot.value as HashMap<*, *>
                                    if(user_select_type=="student")
                                    {
                                        if ( email_et!!.text.startsWith("admin@")) {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Please select admin",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }else{

                                            val intent = Intent(
                                                this@LoginActivity,
                                                StudentDashboardActivity::class.java
                                            )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                            finish()

                                            val editor = getSharedPreferences(
                                                AppUtilObject.PREFS,
                                                MODE_PRIVATE
                                            ).edit()
                                            editor.putString(
                                                AppUtilObject.USER_ID, FirebaseAuth.getInstance().currentUser!!
                                                    .uid)
                                            editor.putString(AppUtilObject.USER_NAME,td["user_name"].toString())
                                            editor.putString(AppUtilObject.USER_EMAIL,td["user_email"].toString())
                                            editor.putString(AppUtilObject.USER_Type, td["user_type"].toString())
                                            editor.putBoolean(AppUtilObject.IS_LOGIN,true)

                                            editor.apply()
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Login Successful ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    else { //admin
                                        if (!email_et!!.text.startsWith("admin@")) {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Please select student",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }else {
                                            val intent = Intent(
                                                this@LoginActivity,
                                                AdminDashboardActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()

                                            val editor = getSharedPreferences(
                                                AppUtilObject.PREFS,
                                                MODE_PRIVATE
                                            ).edit()
                                            editor.putString(
                                                AppUtilObject.USER_ID, FirebaseAuth.getInstance().currentUser!!
                                                    .uid)
                                            editor.putString(AppUtilObject.USER_NAME,td["user_name"].toString())
                                            editor.putString(AppUtilObject.USER_EMAIL,td["user_email"].toString())
                                            editor.putString(AppUtilObject.USER_Type, td["user_type"].toString())
                                            editor.putBoolean(AppUtilObject.IS_LOGIN,true)

                                            editor.apply()
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Login Successful ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }



                                } else {

                                    Toast.makeText(this@LoginActivity, "Login Failed!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })


            } else {

                //    progressbar GONE
                login_progress!!.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}