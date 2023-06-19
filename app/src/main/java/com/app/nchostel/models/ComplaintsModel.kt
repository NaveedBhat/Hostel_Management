package com.app.nchostel.models

import java.io.Serializable

data class ComplaintsModel (val id:String,val user_id :String,val title :String,val description:String,val reply_status:String,val reply_text:String ,val date_and_time:String):Serializable