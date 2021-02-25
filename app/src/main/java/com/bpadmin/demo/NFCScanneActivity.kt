/*
 * Copyright (C) 2018 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bpadmin.demo

import android.R
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.clover.cfp.activity.CloverCFPActivity



open class NFCScanneActivity : CloverCFPActivity() {
    private var mToast: Toast?=null
    private var mAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private val TAG = "NFCScanneActivity"
    //var shouldStartnResume =true
    var isDispatched=false
    private lateinit var handlerNfc: Handler
    private lateinit var delayRunnable: Runnable


    override fun onMessage(p0: String?) {
        val intent = Intent()
        intent.action = NfcAdapter.ACTION_TAG_DISCOVERED
        intent.putExtra(NfcAdapter.EXTRA_TAG, p0)
        resolveIntent(intent)
       // onNewIntent(intent)

        //showToast(p0)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setSystemUiVisibility()
        mAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mAdapter == null) {
            showToast("No NFC found")
        }
        mPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass), 0
        )
        handlerNfc = Handler()
        delayRunnable= Runnable {
            mAdapter!!.enableForegroundDispatch(this, mPendingIntent, null, null)
            enableReaderNFC()
        }
    }
    private fun startDelayingfc() {
        handlerNfc.postDelayed(delayRunnable,500)
    }
    //
    fun disableReaderNFC() {
       /* if (mAdapter != null ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAdapter!!.disableReaderMode(this)
                Log.i(TAG, "diableb called")
            }
        }*/
    }

    fun enableReaderNFC() {
        if (mAdapter != null) {
            runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                /*if (mAdapter!!.isEnabled) {
                    disableReaderNFC()
                }*/

                mAdapter!!.enableReaderMode(
                    this,
                    { tag ->
                        val intent = Intent()
                        intent.action = NfcAdapter.ACTION_TAG_DISCOVERED
                        intent.putExtra(NfcAdapter.EXTRA_TAG, tag)
                        resolveIntent(intent)
                    },
                    NfcAdapter.FLAG_READER_NFC_A,
                    null
                )

                Log.i(TAG, "enable called")
            }
        }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "In onResume")
        if (mAdapter != null) {
            if (!mAdapter!!.isEnabled) {
                Log.e(TAG, "NFC not enabled")

                Log.e(TAG, "on resume in "+javaClass.name)
                showToast("NFC not enabled,please reboot the device")
               // restartApp()
                //  setResultAndFinish(RESULT_CANCELED, "NFC not enabled");
            }

         //   enableReaderNFC()
startDelayingfc()
        }

    }


    override fun onPause() {


        handlerNfc.removeCallbacks(delayRunnable)
        if (mAdapter != null) {
          //  disableReaderNFC()
            //if(!isDispatched){
            try {
                mAdapter!!.disableForegroundDispatch(this)
            }catch (e:Exception){
                e.printStackTrace()
            }
           // }
        }
        super.onPause()
    }

    open fun dispacthForground(){
        if (mAdapter != null) {
            isDispatched=true
            //  disableReaderNFC()
            mAdapter!!.disableForegroundDispatch(this)
        }
    }



    open fun resolveIntent(intent: Intent) { /*  String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                String serialNumber =getHex(tag.getId());
                Toast.makeText(this, "TAG ID : " + serialNumber, Toast.LENGTH_SHORT).show();
                setResultAndFinish(RESULT_OK, serialNumber);
            }
        }*/
    }

  /*  public override fun onNewIntent(intent: Intent) {
        Log.d(TAG, "In onNewIntent")
       // setIntent(intent)
        resolveIntent(intent)
    }

   */

    open fun showToast(msg: String?) {
if(mToast!=null) {
   mToast!!.cancel()
}
    mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)

mToast!!.show()
    }

    fun setSystemUiVisibility() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun getProgressDialog(
        context: Context?,
        relativeLayout: RelativeLayout
    ): ProgressBar {
        val progressBar =
            ProgressBar(context, null, R.attr.progressBarStyleLarge)
        progressBar.isIndeterminate = true
        //progressBar.setBackgroundColor(context.resources.getColor())
        progressBar.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.indeterminateTintList = ColorStateList.valueOf(Color.WHITE)
        }
        val params = RelativeLayout.LayoutParams(150, 150)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        relativeLayout.addView(progressBar, params)
        return progressBar
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}