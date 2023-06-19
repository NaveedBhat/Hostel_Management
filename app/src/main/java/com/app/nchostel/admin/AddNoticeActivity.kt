package com.app.nchostel.admin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.NoticeModel
import com.app.nchostel.student.StudentDashboardActivity
import com.app.nchostel.student.ViewNoticeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class AddNoticeActivity : AppCompatActivity() {
    var title_et: EditText? = null
    var upload_img_btn: AppCompatButton? = null
    var view_notice_btn: AppCompatButton? = null
    var imageView: ImageView? = null

    var login_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("notice")
    var submit_notice_btn: AppCompatButton? = null

    //image
    var launcherDpPhoto: ActivityResultLauncher<Intent>? = null
    var uploadTask: UploadTask? = null
    var storage: FirebaseStorage? = null
    var imageUri: Uri? = null

    private val PICk_IMAGE = 1
    var image_path: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notice)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Add Notice"

        storage = FirebaseStorage.getInstance()

        title_et = findViewById(R.id.title_et)
        upload_img_btn = findViewById(R.id.upload_img_btn)
        imageView = findViewById(R.id.imageView)
        login_progress = findViewById(R.id.login_progress)
        submit_notice_btn = findViewById(R.id.submit_notice_btn)
        view_notice_btn = findViewById(R.id.view_notice_btn)



        launcherDpPhoto = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode ==  PICk_IMAGE || result.resultCode == RESULT_OK || result.data != null) {
                val data = result.data!!
                imageUri = data!!.data
                imageView?.setImageURI(imageUri)
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
                        image_path = downLoadUri.toString()
                    }
                }
            }
        }

        upload_img_btn?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcherDpPhoto!!.launch(intent)
        })

        submit_notice_btn?.setOnClickListener {

            if (title_et!!.getText().length == 0) {
                title_et!!.setError("enter title")
            }   else {
                addNotice()
            }

        }

        view_notice_btn?.setOnClickListener {
            val intent = Intent(this@AddNoticeActivity, ViewNoticeActivity::class.java)
            intent.putExtra("intent_from",2)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    fun addNotice() {
        login_progress?.visibility = View.VISIBLE


        val id = databaseReference.push().key as String
        val noticeModel = NoticeModel(
            id,
             title_et!!.text.toString(),
             image_path,
            AppUtilObject.getCurrentDate() + "," + AppUtilObject.getCurrentTime()

        )

        databaseReference
            .child(id).setValue(noticeModel)
            .addOnCompleteListener {
                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AddNoticeActivity,
                    "Notice uploaded Successfully ",
                    Toast.LENGTH_LONG
                ).show()
              /*  val intent =
                    Intent(this@AddNoticeActivity, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()*/
            }.addOnFailureListener{

                login_progress!!.setVisibility(View.GONE)
                Toast.makeText(
                    this@AddNoticeActivity,
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