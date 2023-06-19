package com.app.nchostel.student

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.app.nchostel.R


class EmergencyContatcsActivity : AppCompatActivity() {

    lateinit var warden_rl :RelativeLayout
    lateinit var shafiq_rl :RelativeLayout

    var contact:String="60050 88890"
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contatcs)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Emergency Contacts"

        warden_rl=findViewById(R.id.warden_rl)
        shafiq_rl=findViewById(R.id.shafiq_rl)

        warden_rl.setOnClickListener {
            contact="6005088890"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" +contact)
            startActivity(intent)
        }
        shafiq_rl.setOnClickListener {
            contact="7780873278"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" +contact)
            startActivity(intent)
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}