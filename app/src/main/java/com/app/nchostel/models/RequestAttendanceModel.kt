package com.app.nchostel.models

data class RequestAttendanceModel(val id:String,val user_id:String, val name:String, val course:String, val batch:String, val attendance_status:String,val reason:String, val  date_and_time:String)
