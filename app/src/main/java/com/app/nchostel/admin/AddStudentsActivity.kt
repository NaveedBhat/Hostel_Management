package com.app.nchostel.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.app.nchostel.R
import com.app.nchostel.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class AddStudentsActivity : AppCompatActivity() {

    var name_et: EditText? = null
    var email_et:EditText? = null

    var password_et: EditText? = null
    var course_et:EditText? = null
    var batch_et:EditText? = null
    var enrolment_et:EditText? = null
    var personal_contact_et:EditText? = null
    var guardian_contact_et:EditText? = null
    var room_et:EditText? = null
    var address_et:EditText? = null
    var card_iv:CardView? = null

    lateinit var male_rb:RadioButton
    lateinit var female_rb:RadioButton

    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("users")
    var add_btn: AppCompatButton? = null
    var upload_iv_btn: AppCompatButton? = null
    private var mAuth: FirebaseAuth? = null

    var gender = "male"

    var launcherDpPhoto: ActivityResultLauncher<Intent>? = null
     var uploadTask: UploadTask? = null
    var storage: FirebaseStorage? = null
    var imageUri: Uri? = null

    private val PICk_IMAGE = 1
    var profile_iv: ImageView? = null
    var userModel: UserModel? = null
    var radio_group: RadioGroup? = null
    var photo_url_link=""

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_students)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Add Students"

         //userModel = UserModel()
          mAuth = FirebaseAuth.getInstance()
         storage = FirebaseStorage.getInstance()

         profile_iv = findViewById(R.id.profile_iv)
         card_iv = findViewById(R.id.card_iv)
         name_et = findViewById(R.id.name_et)
         email_et = findViewById(R.id.email_et)
         password_et = findViewById(R.id.password_et)


         course_et = findViewById(R.id.course_et)
         batch_et = findViewById(R.id.batch_et)
         enrolment_et = findViewById(R.id.enrolment_et)
         room_et = findViewById(R.id.room_et)
         personal_contact_et = findViewById(R.id.personal_contact_et)
         guardian_contact_et = findViewById(R.id.guardian_contact_et)
         address_et = findViewById(R.id.address_et)
         login_progress = findViewById(R.id.login_progress)
         radio_group = findViewById(R.id.radio_group)
         male_rb = findViewById(R.id.male_rb)
         female_rb = findViewById(R.id.female_rb)
         add_btn = findViewById(R.id.add_btn)
         upload_iv_btn = findViewById(R.id.upload_iv_btn)


         //////////////////////////////////////
         launcherDpPhoto = registerForActivityResult(
             StartActivityForResult()
         ) { result ->
             if (result.resultCode ==  PICk_IMAGE || result.resultCode == RESULT_OK || result.data != null) {
                 val data = result.data!!
                 imageUri = data!!.data
                 profile_iv?.setImageURI(imageUri)
                 //upload to firebase
                 login_progress?.setVisibility(View.VISIBLE)
                 val reference =
                     storage!!.reference.child("uploads").child(UUID.randomUUID().toString())
                 uploadTask = reference.putFile(imageUri!!)
                 val urlTask = uploadTask!!.continueWithTask { task ->
                     if (!task.isSuccessful) {
                         throw task.exception!!
                     }
                     reference.downloadUrl
                 }.addOnCompleteListener { task ->
                     login_progress?.setVisibility(View.GONE)
                     if (task.isSuccessful) {
                         val downLoadUri = task.result
                         Log.e("uploadedimages", downLoadUri.toString())
                         //  productImagesList.add(downLoadUri.toString());
                         photo_url_link = downLoadUri.toString()
                     }
                 }
             }
         }

         upload_iv_btn?.setOnClickListener(View.OnClickListener {
             val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
             launcherDpPhoto!!.launch(intent)
         })


           radio_group?.setOnCheckedChangeListener { _, checkedId ->
           val radio: RadioButton = findViewById(checkedId)
           when (radio) {
               male_rb -> {
                   gender="male"
               }
               female_rb -> {
                   gender="female"
               }
           }
       }


         add_btn?.setOnClickListener {

             if(photo_url_link.isEmpty())
             {
                 Toast.makeText(this, "Please upload a photo", Toast.LENGTH_SHORT).show()
             }
             else if (name_et!!.getText().length == 0) {
                 name_et!!.setError("enter name")
             } else if (email_et!!.getText().length == 0) {
                 email_et!!.setError("enter email")
             } else if (password_et!!.getText().length == 0) {
                 password_et!!.setError("enter 8 digit password")
             } else if (course_et!!.getText().length == 0) {
                 course_et!!.setError("enter Course")
             } else if (batch_et!!.getText().length == 0) {
                 batch_et!!.setError("enter Batch")
             } else if (enrolment_et!!.getText().length == 0) {
                 enrolment_et!!.setError("enter Enrollment No. ")
             }else if (room_et!!.getText().length == 0) {
                 room_et!!.setError("enter room No. ")
             } else if (personal_contact_et!!.getText().length == 0) {
                 personal_contact_et!!.setError("enter personal contact No. ")
             }else if (guardian_contact_et!!.getText().length == 0) {
                 guardian_contact_et!!.setError("enter contact No. ")
             }else if (address_et!!.getText().length == 0) {
                 address_et!!.setError("enter address ")
             }else
             {
                 signupFirebase()
             }
         }



    }


    private fun signupFirebase() {

        login_progress!!.setVisibility(View.VISIBLE)
        mAuth!!.createUserWithEmailAndPassword(
            email_et!!.text.toString(),
            password_et!!.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel = UserModel(FirebaseAuth.getInstance().currentUser!!.uid,
                    name_et!!.text.toString(),
                    email_et!!.text.toString(),
                    password_et!!.text.toString(),
                    "student",
                    photo_url_link,
                    course_et!!.text.toString(),
                    batch_et!!.text.toString(),
                    enrolment_et!!.text.toString(),
                    personal_contact_et!!.text.toString(),
                    guardian_contact_et!!.text.toString(),
                    address_et!!.text.toString(),
                    gender,
                    room_et!!.text.toString(),
                    System.currentTimeMillis().toString()

                )

                databaseReference
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userModel)
                    .addOnCompleteListener {
                        login_progress!!.setVisibility(View.GONE)
                        Toast.makeText(this@AddStudentsActivity, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@AddStudentsActivity, AdminDashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            } else {
                //    progressbar GONE
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AddStudentsActivity,
                    "Check Email id or Password",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}