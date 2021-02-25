package com.bpadmin.demo

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class FourthActivity : NFCScanneActivity() {
    var REQUEST_CODE_SCAN_RFID = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tittle.setText("Activity D : click on scan button and scan nfc card")
        btn_scan_rfid.setOnClickListener {
            enableReaderNFC()
        }
        btn_next.setText("Home")
        btn_next.setOnClickListener {
            var intent =  Intent(this@FourthActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }
    override fun resolveIntent(intent: Intent) {
        runOnUiThread {

                doProcessRfid(intent)

        }
    }
    fun doProcessRfid(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                val serialNumber = Utils.getHex(tag.id)
                Log.i("++tag", serialNumber)

                runOnUiThread {
                    tv_nfc_tag.setText("Tag : " +serialNumber)
                }
            }else{
                tv_nfc_tag.setText("Tag not found")
            }
        }
    }


}