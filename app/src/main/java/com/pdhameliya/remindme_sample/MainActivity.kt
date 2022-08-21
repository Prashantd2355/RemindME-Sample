package com.pdhameliya.remindme_sample

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pdhameliya.remindme_sample.Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION
import com.pdhameliya.remindme_sample.Constant.remindME
import com.pdhameliya.remindmelibrary.RemindME
import com.pdhameliya.remindmelibrary.helper.PermissionCheck
import com.pdhameliya.remindmelibrary.interfaces.ReminderListner
import com.pdhameliya.remindmelibrary.promises.ReminderResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ReminderListner, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        remindME = RemindME().getInstance(this)!!

        PermissionCheck.requestLocationPermission(this)

        btnLBR.setOnClickListener {
            startActivity(Intent(this, LocationBasedReminderActivity::class.java))
        }

        btnCBR.setOnClickListener {
            startActivity(Intent(this, CalendarEventActivity::class.java))
        }

        btnNBL.setOnClickListener {
            startActivity(Intent(this, LandmarksActivity::class.java))
        }

        btnCB.setOnClickListener {
            startActivity(Intent(this, ContactBookActivity::class.java))
        }

        btnCallBook.setOnClickListener {
            startActivity(Intent(this, PhoneCallReminderActivity::class.java))
        }

        btnStop.setOnClickListener {
            // Do some work here
            if (remindME != null)
                remindME.stopLocationService(this) else
                Toast.makeText(this, "Library is not initialzed properly", Toast.LENGTH_SHORT)
                    .show()

        }


    }

    override fun onReminderSuccess(reminderResult: ReminderResult) {
        Log.i("result", reminderResult.reminderList.toString());
    }

    override fun onReminderError(errorCode: Int, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        when (errorCode) {
            403 -> {
                val intent =
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${packageName}"))
                startActivityForResult( intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
            } else {
            }
        }
    }

    override fun onClick(p0: View?) {
    }
}