package com.app.nchostel.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.UserModel
import com.app.nchostel.student.AttendanceHistoryActivity
import com.app.nchostel.student.StudentDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.HashMap

class ProfileDetailsActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var signUp_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.USERS_TABLE_KEY)

    lateinit var name_tv:TextView
    lateinit var email_tv:TextView
    lateinit var contact_tv:TextView
    lateinit var address_tv:TextView
    lateinit var course_tv:TextView
    lateinit var enrollment_tv:TextView
    lateinit var imageView:ImageView
    lateinit var copy_btn:AppCompatButton
    lateinit var atten_btn:AppCompatButton
    lateinit  var userModel : UserModel
    var user_id_txt : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        mAuth = FirebaseAuth.getInstance()

        imageView= findViewById(R.id.imageView)
        signUp_progress= findViewById(R.id.signUp_progress)
        name_tv= findViewById(R.id.name_tv)
        email_tv= findViewById(R.id.email_tv)
        contact_tv= findViewById(R.id.contact_tv)
        address_tv= findViewById(R.id.address_tv)
        course_tv= findViewById(R.id.course_tv)
        enrollment_tv= findViewById(R.id.enrollment_tv)
        copy_btn= findViewById(R.id.copy_btn)
        atten_btn= findViewById(R.id.atten_btn)
        signUp_progress?.bringToFront()


        copy_btn.setOnClickListener {

            AppUtilObject.copyClipboard(this,user_id_txt.toString())
        }

        if (intent.getIntExtra("my_intent",0)==1) {
            userModel = intent.getSerializableExtra("data") as UserModel
            supportActionBar!!.title=userModel.user_name
            Picasso.with(this)
                .load(userModel.user_image)
                .into(imageView)

            user_id_txt=userModel.user_id
            name_tv.text = userModel.user_name + " , " + userModel.gender
            email_tv.text = "Email : ${userModel.user_email} \nRoom No: ${userModel.room_number}"
            contact_tv.text =
                "Contacts : " + userModel.personal_contact + " , " + userModel.gaurdian_contact
            address_tv.text = "Address : ${userModel.address}"
            course_tv.text = "Course : " + userModel.course + " , " + userModel.batch
            enrollment_tv.text = "Enrollment No : ${userModel.enrollment_no}"

            atten_btn.visibility=View.VISIBLE

        }else if (intent.getIntExtra("my_intent",0)==2)
        {
            getMyProfile(mAuth?.currentUser!!.uid)
        }else
        {

            //view complaint user details
            getMyProfile(intent.getStringExtra("user_id") as String)
        }

        atten_btn.setOnClickListener {
            val intent = Intent(this, AttendanceHistoryActivity::class.java)
            intent.putExtra("user_id",user_id_txt)
            startActivity(intent)
        }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getMyProfile(user_id:String)
    {
        signUp_progress?.visibility= View.VISIBLE
        databaseReference.child(user_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {

                        signUp_progress?.visibility= View.GONE

                        val td = dataSnapshot.value as HashMap<*, *>




                            Picasso.with(this@ProfileDetailsActivity)
                                .load(td["user_image"].toString())
                                .into(imageView)

                        supportActionBar!!.title=td["user_name"].toString()

                           user_id_txt=td["user_id"].toString()

                            name_tv.text = td["user_name"].toString() + " , " + td["gender"].toString()
                            email_tv.text = "Email : ${td["user_email"].toString()} \nRoom No: ${td["room_number"].toString()}"
                            contact_tv.text = "Contacts : "+td["personal_contact"].toString() + " , "+td["gaurdian_contact"].toString()
                            address_tv.text ="Address : ${td["address"].toString()}"
                            course_tv.text ="Course : "+td["course"].toString()+ " , " + td["batch"].toString()
                            enrollment_tv.text ="Enrollment No : ${td["enrollment_no"].toString()}"





                    } else {
                        signUp_progress?.visibility= View.GONE
                        Toast.makeText(this@ProfileDetailsActivity, "Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {  signUp_progress?.visibility= View.GONE}
            })
    }
}