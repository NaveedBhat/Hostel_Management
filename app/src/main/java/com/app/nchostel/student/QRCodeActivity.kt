package com.app.nchostel.student

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.nchostel.R
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.HashMap


class QRCodeActivity : AppCompatActivity() {

    lateinit var qrIV: ImageView
    var mAuth: FirebaseAuth? = null
    var signUp_progress: ProgressBar? = null
    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child(AppUtilObject.USERS_TABLE_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="My QRCode"

        qrIV = findViewById(R.id.idIVQrcode)
        signUp_progress= findViewById(R.id.signUp_progress)
        mAuth = FirebaseAuth.getInstance()

        val preferences = getSharedPreferences(AppUtilObject.PREFS, MODE_PRIVATE)
        var user_id = preferences.getString(AppUtilObject.USER_ID,"");
        var user_name= preferences.getString(AppUtilObject.USER_NAME,"");

        getMyProfile(user_id.toString())

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun generateQR(content: String?, size: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(
                content,
                BarcodeFormat.QR_CODE, size, size
            )
        } catch (e: WriterException) {
            Log.e("generateQR()", e.message.toString())
        }
        return bitmap
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







                      //td["user_id"].toString()

                    //td["user_name"].toString() + " , " + td["gender"].toString()
                    //"Email : ${td["user_email"].toString()} \nRoom No: ${td["room_number"].toString()}"
                     //"Contacts : "+td["personal_contact"].toString() + " , "+td["gaurdian_contact"].toString()
                     //"Address : ${td["address"].toString()}"
                     //"Course : "+td["course"].toString()+ " , " + td["batch"].toString()
                      //"Enrollment No : ${td["enrollment_no"].toString()}"
                        var  mainObject = JSONObject()
                        var  dataObject = JSONObject()
                        dataObject.put("user_id",td["user_id"].toString())
                        dataObject.put("name",td["user_name"].toString())
                        dataObject.put("batch",td["batch"].toString())
                        dataObject.put("course",td["course"].toString())
                        mainObject.put("data",dataObject)

                        qrIV.setImageBitmap(generateQR(mainObject.toString(),512))

                        qrIV.setOnClickListener {
                            Log.e("chhchchhchc"," ${mainObject.toString()}")
                        }




                    } else {
                        signUp_progress?.visibility= View.GONE
                        Toast.makeText(this@QRCodeActivity, "Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {  signUp_progress?.visibility= View.GONE}
            })
    }
}