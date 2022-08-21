package com.pdhameliya.remindme_sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.pdhameliya.remindmelibrary.actions.CRActionParams
import com.pdhameliya.remindmelibrary.helper.CRData
import com.pdhameliya.remindmelibrary.interfaces.ReminderListner
import com.pdhameliya.remindmelibrary.promises.ReminderResult
import kotlinx.android.synthetic.main.activity_add_call_reminder.btSubmit
import kotlinx.android.synthetic.main.activity_add_call_reminder.chkAlert
import kotlinx.android.synthetic.main.activity_add_call_reminder.chkNotification
import kotlinx.android.synthetic.main.activity_add_call_reminder.chkSound
import kotlinx.android.synthetic.main.activity_add_call_reminder.etContactLattitude
import kotlinx.android.synthetic.main.activity_add_call_reminder.etContactLongitude
import kotlinx.android.synthetic.main.activity_add_call_reminder.etContactName
import kotlinx.android.synthetic.main.activity_add_call_reminder.etContactNo
import kotlinx.android.synthetic.main.activity_add_call_reminder.ivSelectLocation

class AddCallReminderActivity : AppCompatActivity(),ReminderListner {
    var isUpdate: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_call_reminder)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.add_call_reminder)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        isUpdate = intent.getBooleanExtra("isUpdate", false)

        main()
    }
    private fun main() {
        if (isUpdate) {
            etContactName.setText(Constant.crData.CRName)
            etContactNo.setText(Constant.crData.CRMobNo)
            etContactLattitude.setText(Constant.crData.CRLattitude.toString())
            etContactLongitude.setText(Constant.crData.CRLongitude.toString())
            chkNotification.isChecked = Constant.crData.CRIsNotification == true
            chkAlert.isChecked = Constant.crData.CRIsAlert == true
            chkSound.isChecked = Constant.crData.CRIsSound == true
        }
        chkAlert.setOnCheckedChangeListener { buttonView, isChecked ->
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
            etContactLattitude.setText(""+latitude)
            etContactLongitude.setText(""+longitude)
        }
    }
    private fun checkValid(): Boolean {
        return when {
            etContactName.text.isEmpty() -> {
                etContactName.error = getString(R.string.enter_contact_name_error)
                etContactName.requestFocus()
                false
            }
            etContactNo.text.isEmpty() -> {
                etContactNo.error = getString(R.string.enter_contact_no_error)
                etContactNo.requestFocus()
                false
            }
            etContactLattitude.text.isEmpty() -> {
                etContactLattitude.error = getString(R.string.enter_latitude_error)
                etContactLattitude.requestFocus()
                false
            }
            etContactLongitude.text.isEmpty() -> {
                etContactLongitude.error = getString(R.string.enter_longitude_error)
                etContactLongitude.requestFocus()
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
                Constant.remindME.updateCallReminder(this,
                    CRData(
                        etContactName.text.toString(),
                        etContactNo.text.toString(),
                        etContactLattitude.text.toString().toDouble(),
                        etContactLongitude.text.toString().toDouble(),
                        chkNotification.isChecked,
                        chkAlert.isChecked,
                        chkSound.isChecked,
                        false,
                        Constant.crData.CreatedDate,
                        Constant.crData.UpdatedDate,
                        Constant.crData.CRId
                    ),this
                )
            } else {
                Constant.remindME.createCallReminder(
                    CRActionParams(
                        etContactName.text.toString(),
                        etContactNo.text.toString(),
                        etContactLattitude.text.toString().toDouble(),
                        etContactLongitude.text.toString().toDouble(),
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