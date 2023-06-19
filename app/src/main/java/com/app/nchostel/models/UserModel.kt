package com.app.nchostel.models

import java.io.Serializable

data class UserModel(val user_id:String,val user_name:String,val user_email:String,val user_password : String,
                        val user_type:String, val user_image:String, val course :String,val batch:String,val enrollment_no :String
                        , val personal_contact:String,val gaurdian_contact :String,val address:String,
                     val gender:String,val room_number:String,val date_and_time:String
                     ) : Serializable