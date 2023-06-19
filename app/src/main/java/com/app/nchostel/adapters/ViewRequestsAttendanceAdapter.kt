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
import com.app.nchostel.admin.*
import com.app.nchostel.apputil.AppUtilObject
import com.app.nchostel.models.AttendanceModel
import com.app.nchostel.models.ComplaintsModel
import com.app.nchostel.models.RequestAttendanceModel
import com.app.nchostel.models.UserModel
import com.app.nchostel.student.AttendanceHistoryDetailsActivity
import com.app.nchostel.student.ImageZoomActivity
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ViewRequestsAttendanceAdapter(private val context: Activity, private val mList: List<RequestAttendanceModel>) : RecyclerView.Adapter<ViewRequestsAttendanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attendance_requests_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

         val ItemsViewModel = mList[position]
         holder.name_tv.text = ItemsViewModel.name+ "("+ItemsViewModel.course + " / " + ItemsViewModel.batch+")"
         holder.reason_tv.text ="Reason : " +  ItemsViewModel.reason
         holder.course_tv.text = "Status : ${ItemsViewModel.attendance_status}"

        holder.accept_btn.setOnClickListener{
            acceptAttendUser(position, viewHolder = holder)
        }

        holder.reject_btn.setOnClickListener {
            deleteRequest(position,holder)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val name_tv: TextView = itemView.findViewById(R.id.name_tv)
        val course_tv: TextView = itemView.findViewById(R.id.course_tv)
        val reason_tv: TextView = itemView.findViewById(R.id.reason_tv)
        val accept_btn: TextView = itemView.findViewById(R.id.accept_btn)
        val reject_btn: TextView = itemView.findViewById(R.id.reject_btn)

    }


    private fun deleteRequest(
        position: Int,
        viewHolder: ViewHolder
    ) {
        val database = FirebaseDatabase.getInstance()
        val mDatabaseRef = database.reference
            .child("attendance_requests")
            .child(mList.get(position).id)
        mDatabaseRef.removeValue()
        viewHolder.itemView.setVisibility(View.GONE)
        Toast.makeText(context, "Request has been rejected!", Toast.LENGTH_SHORT).show()
        //mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)
    }



    private fun acceptAttendUser(
        position: Int,
        viewHolder: ViewHolder
    ) {

        var attendanceModel = AttendanceModel(
            mList.get(position).user_id,
            mList.get(position).name,
            mList.get(position).course,
            mList.get(position).batch,
            mList.get(position).attendance_status,
            AppUtilObject.getCurrentDateAndTime().toString(),
        )

        attendanceProcess(attendanceModel,position, holder = viewHolder)

    }


    //make attendance on accept
    fun attendanceProcess(attendanceModel: AttendanceModel,position: Int,holder: ViewHolder)
    {
        var mDatabase = FirebaseDatabase.getInstance()
        var databaseReference = mDatabase.reference.child("attendance")
        var databaseReferenceAdmin = mDatabase.reference.child("attendance_admin")
         databaseReference
            .child(attendanceModel.user_id).child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(AppUtilObject.getCurrentDayMonthYearForAttendance().toString()).setValue(attendanceModel)
            .addOnCompleteListener {

                //for admin
                databaseReferenceAdmin
                    .child(AppUtilObject.getCurrentMonthYearForAttendance().toString()).child(AppUtilObject.getCurrentDayMonthYearForAttendance().toString()) .child(attendanceModel.user_id).setValue(attendanceModel)
                    .addOnCompleteListener {
                         Toast.makeText(context, "Attendance Accepted Successfully", Toast.LENGTH_LONG).show()
                        deleteRequest(position = position,holder)
                        val intent = Intent(context, ViewAttendanceRequestsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                        context. startActivity(intent)
                        context.finish()
                    }


            }.addOnFailureListener({


                //    progressbar GONE
                 Toast.makeText(
                    context,
                    "Attendance Failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()

            })


    }


}