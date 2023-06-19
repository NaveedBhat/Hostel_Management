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
import com.app.nchostel.models.UserModel
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ViewComplaintsAdapter(private val context: Activity, private val mList: List<ComplaintsModel>) : RecyclerView.Adapter<ViewComplaintsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_complaints_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        if (ItemsViewModel.reply_status.equals("1"))holder.imageView.setImageResource(R.drawable.yes_reply_ic)
        else holder.imageView.setImageResource(R.drawable.pending_reply_ic)

        // sets the text to the textview from our itemHolder class
        holder.title_tv!!.text = ItemsViewModel.title
        holder.desc_tv!!.text = ItemsViewModel.description
        holder.date_tv!!.text=ItemsViewModel.date_and_time


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ComplaintDetailsActivity::class.java)
            intent.putExtra("data",mList.get(position))
            intent.putExtra("my_intent",1)
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
        val desc_tv: TextView = itemView.findViewById(R.id.desc_tv)
        val date_tv: TextView = itemView.findViewById(R.id.date_tv)
      /*  val delete_btn: AppCompatButton = itemView.findViewById(R.id.delete_btn)
        val view_profile_btn: AppCompatButton = itemView.findViewById(R.id.view_profile_btn)*/
    }

    private fun deleteUser(
        position: Int,
        viewHolder: ViewHolder
    ) {
        val database = FirebaseDatabase.getInstance()
        val mDatabaseRef = database.reference
            .child(AppUtilObject.USERS_TABLE_KEY)
            .child(mList.get(position).user_id)
        mDatabaseRef.removeValue()
        viewHolder.itemView.setVisibility(View.GONE)
        Toast.makeText(context, "Student has been deleted!", Toast.LENGTH_SHORT).show()
        //mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)
    }

}