package com.app.nchostel.apputil

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Environment
import android.text.ClipboardManager
import android.util.Log
import android.widget.Toast
import com.app.nchostel.models.AttendanceModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.*


object AppUtilObject {

        val STORAGE_PATH_UPLOADS = "uploads/"
        val DATABASE_PATH_UPLOADS = "uploads"

        val USERS_TABLE_KEY = "users"
        val COMPLAINTS_TABLE_KEY = "complaints"
        val PREFS = "prefs"
        val USER_ID = "user_id"
        val USER_NAME = "user_name"
        val USER_EMAIL = "user_email"
        val USER_Type = "user_type"
        val IS_LOGIN = "is_login"

        fun startActivity(context: Context, cls: Class<*>?) {
            val intent = Intent(context, cls)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }


        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            println("Current time => $c")
            val df = SimpleDateFormat("dd MMM yyyy")
            return df.format(c)
        }

        fun getCurrentTime(): String? {
            return SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        }

     fun getCurrentDateAndTime():String?{
         val c = Calendar.getInstance().time
         println("Current time => $c")
         val df = SimpleDateFormat("dd MMM yyyy , hh:mm:ss a")
         return df.format(c)
     }



     fun getCurrentDayMonthYearForAttendance():String?
     {
         val sdf = SimpleDateFormat("dd_MMMM_yyyy")
         val currentDate = sdf.format(Date())
         System.out.println(" C DATE is  "+currentDate)

         return  currentDate
     }
     fun getCurrentMonthYearForAttendance():String?
     {
         val sdf = SimpleDateFormat("MMMM_yyyy")
         val currentDate = sdf.format(Date())
         System.out.println(" C DATE is  "+currentDate)

         return  currentDate
     }


      fun copyClipboard(context: Context, text: String) {
         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
             val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
             clipboard.text = text
         } else {
             val clipboard =
                 context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
             val clip = ClipData.newPlainText("Copied Text", text)
             clipboard.setPrimaryClip(clip)

             Toast.makeText(context, "User id copied!!!", Toast.LENGTH_SHORT).show()
         }
     }

     private val ROW_INC = 20F
     private val COL_INC = 15F
     fun jsonFormat(jsonStr: String): String {

         var indentCount = 0

         val builder = StringBuilder()
         for (c in jsonStr) {
             if (indentCount > 0 && '\n' == builder.last()) {
                 for (x in 0..indentCount) builder.append("    ")
             }
             when (c) {
                 '{', '[' -> {
                     builder.append(c).append("\n")
                     indentCount++
                 }
                 ',' -> builder.append(c).append("\n")
                 '}', ']' -> {
                     builder.append("\n")
                     indentCount--
                     for (x in 0..indentCount) builder.append("    ")
                     builder.append(c)
                 }
                 else -> builder.append(c)
             }
         }

         return builder.toString()
     }


     fun createPdf(dateS:String ,attendanceDetailsList: ArrayList<AttendanceModel>,context: Context) {

         var x=30f
         var y=100f
         var sty=100f
         var batchY=100f

          val document = PdfDocument()
          var pageInfo = PageInfo.Builder(500, 700, 1).create()
          var page = document.startPage(pageInfo)
          var canvas: Canvas = page.canvas

         //date title
         var dateTitlePaint = Paint()
         dateTitlePaint.textSize=20f

         dateTitlePaint.textAlign= Paint.Align.CENTER
          canvas.drawText(dateS.uppercase(),  canvas.width / 2f,
             50f, dateTitlePaint)


         //present students
         var paintPre = Paint()
         paintPre.textSize=14f
         paintPre.textAlign= Paint.Align.CENTER
         var presentStu=0;


         for(ss in attendanceDetailsList) {
             var paint = Paint()
             canvas.drawText(ss.name, x, y, paint)

             var paintBatch = Paint()
             canvas.drawText(ss.course + " / " + ss.batch, 250f, batchY, paintBatch)

             var paintSt = Paint()

           if (ss.attendance_status.equals("absent"))
           {
               paintSt.color = Color.RED
           }
         else{
              presentStu +=1;
              paintSt.color = Color.GREEN
          }

             paintSt.style = Paint.Style.FILL
             canvas.drawText(ss.attendance_status.uppercase(), 400f, sty, paintSt)

             y +=paint.textSize * 2f
             batchY +=paintBatch.textSize * 2f
             sty +=paintSt.textSize * 2f




         }
         canvas.drawText("Present Students : ${presentStu}", 80f,80f, paintPre)
         document.finishPage(page)
         var directory_path: String? = null
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
         {
             directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + "/NCHostel/"
         }else {
             directory_path = Environment.getExternalStorageDirectory().path + "/NCHostel/"
         }
         val file = File(directory_path)
         if (!file.exists()) {
             file.mkdirs()
         }
         val targetPdf = directory_path + "${dateS}_attendance.pdf"
         val filePath = File(targetPdf)
         try {
             document.writeTo(FileOutputStream(filePath))
             Log.e("doenenen","Done pdf")
             Toast.makeText(context, "Pdf Generated in NCHostel Folder", Toast.LENGTH_LONG).show()
          } catch (e: IOException) {
             Log.e("main", "error " + e.toString())
          }
         // close the document
         document.close()
     }

    }


