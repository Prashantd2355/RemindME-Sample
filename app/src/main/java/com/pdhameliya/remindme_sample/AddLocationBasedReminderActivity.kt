package com.pdhameliya.remindme_sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pdhameliya.remindmelibrary.actions.LBRActionParams
import com.pdhameliya.remindmelibrary.helper.LBRData
import com.pdhameliya.remindmelibrary.interfaces.ReminderListner
import com.pdhameliya.remindmelibrary.promises.ReminderResult
import kotlinx.android.synthetic.main.activity_add_location_based_reminder.*

class AddLocationBasedReminderActivity : AppCompatActivity(), ReminderListner {
    var isUpdate: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_based_reminder)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.add_location_based_reminder)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        isUpdate = intent.getBooleanExtra("isUpdate", false)

        main()
    }
    private fun main() {
        if (isUpdate) {
            etReminderTitle.setText(Constant.lbrData.LBRTitle)
            etReminderDesc.setText(Constant.lbrData.LBRDesc)
            etReminderLattitude.setText(Constant.lbrData.LBRLattitude.toString())
            etReminderLongitude.setText(Constant.lbrData.LBRLongitude.toString())
            chkNotification.isChecked = Constant.lbrData.LBRIsNotification == true
            chkAlert.isChecked = Constant.lbrData.LBRIsAlert == true
            chkSound.isChecked = Constant.lbrData.LBRIsSound == true
        }
        chkAlert.setOnCheckedChangeListener { buttonView, isChecked ->
            //You need to check draw overlay permission for showing alert window while app is closed.
//            Toast.makeText(this, isChecked.toString(), Toast.LENGTH_SHORT).show()
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        chkAlert.isChecked = false
                        val intent =
                            Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:${packageName}")
                            )
                        startActivityForResult(intent, Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION)
                    }
                }
            }
        }
        ivSelectLocation.setOnClickListener{
            val intent = Intent(this, SelectLocationActivity::class.java)
            resultLauncher.launch(intent)
        }
        btSubmit.setOnClickListener {
            if (checkValid()) {
                insertData()
            }
        }
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val latitude = data!!.getDoubleExtra("lattitude",0.0)
            val longitude = data!!.getDoubleExtra("longitude",0.0)
            etReminderLattitude.setText(""+latitude)
            etReminderLongitude.setText(""+longitude)
        }
    }
    private fun checkValid(): Boolean {
        return when {
            etReminderTitle.text.isEmpty() -> {
                etReminderTitle.error = getString(R.string.enter_title_error)
                etReminderTitle.requestFocus()
                false
            }
            etReminderDesc.text.isEmpty() -> {
                etReminderDesc.error = getString(R.string.enter_desc_error)
                etReminderDesc.requestFocus()
                false
            }
            etReminderLattitude.text.isEmpty() -> {
                etReminderLattitude.error = getString(R.string.enter_latitude_error)
                etReminderLattitude.requestFocus()
                false
            }
            etReminderLongitude.text.isEmpty() -> {
                etReminderLongitude.error = getString(R.string.enter_longitude_error)
                etReminderLongitude.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun insertData() {
        if (Constant.remindME != null) {
            if (isUpdate) {
                Constant.remindME.updateLocationBasedReminder(this,
                    LBRData(
                        etReminderTitle.text.toString(),
                        etReminderDesc.text.toString(),
                        etReminderLattitude.text.toString().toDouble(),
                        etReminderLongitude.text.toString().toDouble(),
                        chkNotification.isChecked,
                        chkAlert.isChecked,
                        chkSound.isChecked,
                        false,
                        Constant.lbrData.CreatedDate,
                        Constant.lbrData.UpdatedDate,
                        LBRId = Constant.lbrData.LBRId
                    ),this
                )

            } else {
                Constant.remindME.createLocationBasedReminder(
                    LBRActionParams(
                        etReminderTitle.text.toString(),
                        etReminderDesc.text.toString(),
                        etReminderLattitude.text.toString().toDouble(),
                        etReminderLongitude.text.toString().toDouble(),
                        chkAlert.isChecked,
                        isSoundEnabled = chkSound.isChecked,
                        isNotification = chkNotification.isChecked
                    ), this, this
                )
            }
        }else
            Toast.makeText(this, "Library is not initialized properly", Toast.LENGTH_SHORT)
                .show()
    }


    override fun onReminderSuccess(reminderResult: ReminderResult) {
        Log.i("result", reminderResult.reminderList.toString())
        runOnUiThread {
            tvResult.text = reminderResult.reminderList.toString()
        }
        runOnUiThread { finish() }

    }

    override fun onReminderError(errorCode: Int, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        when (errorCode) {
            403 -> {
                val intent =
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${packageName}")
                    )
                startActivityForResult(intent, Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                chkAlert.isChecked = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}