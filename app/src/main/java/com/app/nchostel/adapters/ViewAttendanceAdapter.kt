package com.app.nchostel.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.app.nchostel.R
import com.app.nchostel.admin.AdminDashboardActivity
import com.app.nchostel.admin.ComplaintDetailsActivity
import com.app.nchostel.admin.ProfileDetailsActivity
import com.app.nchostel.admin.ViewAttendanceDetailsActivity
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.UserModel
import com.app.nchostel.student.AttendanceHistoryDetailsActivity
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ViewAttendanceAdapter(private val context: Activity, private val mList: List<String>) : RecyclerView.Adapter<ViewAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_list_row_one, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
         holder.title_tv!!.text = ItemsViewModel.replace("_"," ")
          holder.itemView.setOnClickListener {
            val intent = Intent(context, ViewAttendanceDetailsActivity::class.java)
             intent.putExtra("month_key",ItemsViewModel)
            context. startActivity(intent)

        }
     }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title_tv: TextView = itemView.findViewById(R.id.title_tv)

    }



}