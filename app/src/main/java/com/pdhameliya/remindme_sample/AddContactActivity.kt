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
import com.pdhameliya.remindmelibrary.actions.CBActionParams
import com.pdhameliya.remindmelibrary.helper.CBData
import com.pdhameliya.remindmelibrary.interfaces.ReminderListner
import com.pdhameliya.remindmelibrary.promises.ReminderResult
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_add_contact.btSubmit
import kotlinx.android.synthetic.main.activity_add_contact.chkAlert
import kotlinx.android.synthetic.main.activity_add_contact.chkNotification
import kotlinx.android.synthetic.main.activity_add_contact.chkSound
import kotlinx.android.synthetic.main.activity_add_contact.ivSelectLocation

class AddContactActivity : AppCompatActivity(), ReminderListner {
    var isUpdate: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.add_contact)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        isUpdate = intent.getBooleanExtra("isUpdate", false)

        main()
    }
    private fun main() {
        if (isUpdate) {
            etContactTitle.setText(Constant.cbData.CBTitle)
            etContactDesc.setText(Constant.cbData.CBDesc)
            etContactName.setText(Constant.cbData.CBName)
            etContactNo.setText(Constant.cbData.CBMobNo)
            etContactLattitude.setText(Constant.cbData.CBLattitude.toString())
            etContactLongitude.setText(Constant.cbData.CBLongitude.toString())
            chkNotification.isChecked = Constant.cbData.CBIsNotification == true
            chkAlert.isChecked = Constant.cbData.CBIsAlert == true
            chkSound.isChecked = Constant.cbData.CBIsSound == true
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
            etContactTitle.text.isEmpty() -> {
                etContactTitle.error = getString(R.string.enter_contact_title_error)
                etContactTitle.requestFocus()
                false
            }
            etContactDesc.text.isEmpty() -> {
                etContactDesc.error = getString(R.string.enter_contact_desc_error)
                etContactDesc.requestFocus()
                false
            }
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
                Constant.remindME.updateContactBook(this,
                    CBData(
                        etContactTitle.text.toString(),
                        etContactDesc.text.toString(),
                        etContactName.text.toString(),
                        etContactNo.text.toString(),
                        etContactLattitude.text.toString().toDouble(),
                        etContactLongitude.text.toString().toDouble(),
                        Constant.cbData.CBNoOfVisit,
                        chkNotification.isChecked,
                        chkAlert.isChecked,
                        chkSound.isChecked,
                        false,
                        Constant.cbData.CreatedDate,
                        Constant.cbData.UpdatedDate,
                        Constant.cbData.CBId
                    ),this
                )
            } else {
                Constant.remindME.addContact(
                    CBActionParams(
                        etContactTitle.text.toString(),
                        etContactDesc.text.toString(),
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