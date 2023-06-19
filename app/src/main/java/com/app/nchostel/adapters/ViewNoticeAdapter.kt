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
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.NoticeModel
import com.app.nchostel.models.UserModel
import com.app.nchostel.student.ImageZoomActivity
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ViewNoticeAdapter(private val context: Activity, private val mList: List<NoticeModel>,private val intentfrom:Int) : RecyclerView.Adapter<ViewNoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_notice_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        if(!mList.get(position).image.isEmpty()) {
            holder.imageView.visibility = View.VISIBLE

            Picasso.with(context)
                .load(mList.get(position).image)
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ImageZoomActivity::class.java)
                intent.putExtra("image",mList.get(position).image)
                context. startActivity(intent)
            }
        }else
        {
            holder.imageView.visibility = View.GONE
         }
        if (intentfrom==2) {


            holder.delete_btn.visibility=View.VISIBLE
        }




        holder.title_tv!!.text = ItemsViewModel.title
         holder.date_tv!!.text=ItemsViewModel.date_and_time

        holder.delete_btn.setOnClickListener(View.OnClickListener { deleteNotice(position, holder) })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title_tv: TextView = itemView.findViewById(R.id.title_tv)
         val date_tv: TextView = itemView.findViewById(R.id.date_tv)
        val delete_btn: AppCompatButton = itemView.findViewById(R.id.delete_btn)
     }

    private fun deleteNotice(
        position: Int,
        viewHolder: ViewHolder
    ) {
        val database = FirebaseDatabase.getInstance()
        val mDatabaseRef = database.reference
            .child("notice")
           .child(mList.get(position).id)
        mDatabaseRef.removeValue()
        viewHolder.itemView.setVisibility(View.GONE)
        Toast.makeText(context, "${mList.get(position).title} notice has been deleted!", Toast.LENGTH_SHORT).show()

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)
        notifyDataSetChanged()
    }

}