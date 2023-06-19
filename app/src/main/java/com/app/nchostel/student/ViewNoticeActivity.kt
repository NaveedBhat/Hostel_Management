package com.app.nchostel.student

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.adapters.ViewNoticeAdapter
import com.app.nchostel.models.NoticeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ViewNoticeActivity : AppCompatActivity() {

    var mDatabase = FirebaseDatabase.getInstance()
    var databaseReference = mDatabase.reference.child("notice")

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "New Notice Recieved."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notice)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Notice"
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
                 val userModelList = ArrayList<NoticeModel>()
                if (dataSnapshot.exists()) {

                    Log.e("saawewe", dataSnapshot.getValue().toString());
                    recyclerView!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.GONE

                    val td = dataSnapshot.value as HashMap<*, *>
                    for (key in td.keys) {
                        val studentList = td[key] as HashMap<*, *>

                        val noticeModel = NoticeModel(
                            studentList["id"].toString(),
                            studentList["title"].toString(),
                            studentList["image"].toString(),

                            studentList["date_and_time"].toString(),

                            )

                        userModelList.add(noticeModel!!)
                    }

                    Collections.reverse(userModelList)
                    val adapter = ViewNoticeAdapter(
                        this@ViewNoticeActivity,
                        userModelList,
                        intent.getIntExtra("intent_from", 0)
                    )
                    recyclerView!!.setHasFixedSize(true)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ViewNoticeActivity)
                    recyclerView!!.adapter = adapter
                } else {
                    Toast.makeText(this@ViewNoticeActivity, "No List Found.", Toast.LENGTH_SHORT)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewNoticeActivity, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
                progressBar!!.visibility = View.GONE
            }
        })
    }




    fun noteeeeess()
    {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, ViewNoticeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId).setContentTitle("NOTIFICATION  " +
                    "dffd").setContentText("Test Notification").setSmallIcon(R.drawable .logo_ic).setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable
                .ic_launcher_background)).setContentIntent(pendingIntent)
        }
        notificationManager.notify(12345, builder.build())
    }




}