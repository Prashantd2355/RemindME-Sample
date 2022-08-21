package com.pdhameliya.remindme_sample

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pdhameliya.remindme_sample.adapter.CeAdapter
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.actions.CEActionParams
import com.pdhameliya.remindmelibrary.helper.CEData
import com.pdhameliya.remindmelibrary.interfaces.CalenderDayClickListener
import com.pdhameliya.remindmelibrary.interfaces.ReminderListner
import com.pdhameliya.remindmelibrary.promises.ReminderResult
import com.pdhameliya.remindmelibrary.remiders.calendarview.helper.TimeUtil
import com.pdhameliya.remindmelibrary.remiders.calendarview.model.DayContainerModel
import kotlinx.android.synthetic.main.activity_calendar_event.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarEventActivity : AppCompatActivity(), ReminderListner,ReminderListListner {

    private var etEventLattitude:EditText?=null
    private var etEventLongitude:EditText?=null
    private var etEventTitle:EditText?=null
    var etEventDesc:EditText?=null
    var etEventTime:EditText?=null
    var chkNotification:CheckBox?=null
    var chkAlert:CheckBox?=null
    var chkSound:CheckBox?=null

    var mSelectedTimeMillisecond: Long?=null
    var mSelectedDateMillisecond : Long?=null
    lateinit var ceData: CEData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_event)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.calendar_based_reminder)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        main()
    }

    private fun main() {
        mSelectedDateMillisecond = eventCalendar.getCurrentDateInMillisecond()

        getCalendarEvents(mSelectedDateMillisecond!!)

        eventCalendar.initCalderItemClickCallback(object : CalenderDayClickListener {
            override fun onGetDay(dayContainerModel: DayContainerModel?) {
                Log.d("TAG", dayContainerModel!!.getDate().toString())
                mSelectedDateMillisecond = dayContainerModel.getTimeInMillisecond()
                getCalendarEvents(mSelectedDateMillisecond!!)
            }
        })

        fbAddEvent.setOnClickListener{
            showBottomSheetDialog(false)
        }
    }
    private fun showBottomSheetDialog(isUpdate:Boolean) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setContentView(R.layout.add_calendar_event_dialog)
        val ivClose = bottomSheetDialog.findViewById<ImageView>(R.id.ivClose)
        val ivSelectLocation = bottomSheetDialog.findViewById<ImageView>(R.id.ivSelectLocation)
        etEventTitle = bottomSheetDialog.findViewById(R.id.etEventTitle)
        etEventDesc = bottomSheetDialog.findViewById(R.id.etEventDesc)
        etEventTime = bottomSheetDialog.findViewById(R.id.etEventTime)
        etEventLattitude = bottomSheetDialog.findViewById(R.id.etEventLattitude)
        etEventLongitude = bottomSheetDialog.findViewById(R.id.etEventLongitude)
        chkNotification = bottomSheetDialog.findViewById(R.id.chkNotification)
        chkAlert = bottomSheetDialog.findViewById(R.id.chkAlert)
        chkSound = bottomSheetDialog.findViewById(R.id.chkSound)
        val btSubmit = bottomSheetDialog.findViewById<Button>(R.id.btSubmit)

        if(isUpdate){
            etEventTitle?.setText(ceData.CETitle)
            etEventDesc?.setText(ceData.CEDesc)
            etEventTime?.setText(TimeUtil().getTime(ceData.CETime!!))
            etEventLattitude?.setText(ceData.CELattitude.toString())
            etEventLongitude?.setText(ceData.CELongitude.toString())
            chkNotification?.isChecked = ceData.CEIsNotification == true
            chkAlert?.isChecked = ceData.CEIsAlert == true
            chkSound?.isChecked = ceData.CEIsSound == true
        }

        ivClose?.setOnClickListener{
            bottomSheetDialog.dismiss()
        }

        chkAlert?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        chkAlert!!.isChecked = false
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

        etEventTime?.setOnClickListener{
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            var mTimePicker = TimePickerDialog(this,{ timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = selectedHour
                calendar[Calendar.MINUTE] = selectedMinute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                mSelectedTimeMillisecond = calendar.timeInMillis
                etEventTime!!.setText("$selectedHour:$selectedMinute")
            },hour,
                minute,
                true)

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        ivSelectLocation?.setOnClickListener{
            val intent = Intent(this, SelectLocationActivity::class.java)
            resultLauncher.launch(intent)
        }

        btSubmit?.setOnClickListener {
            if (checkValid()) {
                insertData(isUpdate)
            }
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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
                chkAlert?.isChecked = true
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val latitude = data!!.getDoubleExtra("lattitude",0.0)
            val longitude = data!!.getDoubleExtra("longitude",0.0)
            etEventLattitude?.setText(""+latitude)
            etEventLongitude?.setText(""+longitude)
        }
    }

    private fun checkValid(): Boolean {
        return when {
            etEventTitle!!.text.isEmpty() -> {
                etEventTitle!!.error = getString(R.string.enter_event_title_error)
                etEventTitle!!.requestFocus()
                false
            }
            etEventDesc!!.text.isEmpty() -> {
                etEventDesc!!.error = getString(R.string.enter_event_desc_error)
                etEventDesc!!.requestFocus()
                false
            }
            mSelectedDateMillisecond==0L -> {
                Toast.makeText(this, getString(R.string.select_event_date_error) , Toast.LENGTH_SHORT).show()
                false
            }
            mSelectedTimeMillisecond==0L -> {
                Toast.makeText(this, getString(R.string.select_event_time_error) , Toast.LENGTH_SHORT).show()
                false
            }
            etEventLattitude!!.text.isEmpty() -> {
                etEventLattitude!!.error = getString(R.string.enter_latitude_error)
                etEventLattitude!!.requestFocus()
                false
            }
            etEventLongitude!!.text.isEmpty() -> {
                etEventLongitude!!.error = getString(R.string.enter_longitude_error)
                etEventLongitude!!.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun insertData(isUpdate:Boolean) {
        if (Constant.remindME != null) {
            if (isUpdate) {
                Constant.remindME.updateCalendarEvent(this,
                    CEData(
                        etEventTitle?.text.toString(),
                        etEventDesc?.text.toString(),
                        mSelectedDateMillisecond!!,
                        mSelectedTimeMillisecond!!,
                        etEventLattitude?.text.toString().toDouble(),
                        etEventLongitude?.text.toString().toDouble(),
                        chkNotification?.isChecked,
                        chkAlert?.isChecked,
                        chkSound!!.isChecked,
                        false,
                        ceData.CreatedDate,
                        ceData.UpdatedDate,
                        ceData.CEId
                    ),this
                )
            } else {
                Constant.remindME.createCalendarEvent(
                    CEActionParams(
                        etEventTitle?.text.toString(),
                        etEventDesc?.text.toString(),
                        mSelectedDateMillisecond!!,
                        mSelectedTimeMillisecond!!,
                        etEventLattitude?.text.toString().toDouble(),
                        etEventLongitude?.text.toString().toDouble(),
                        isShowAlert = chkAlert!!.isChecked,
                        isSoundEnabled = chkSound!!.isChecked,
                        isNotification = chkNotification!!.isChecked
                    ), this, this
                )
            }

        }else
            Toast.makeText(this, "Library is not initialized properly", Toast.LENGTH_SHORT)
                .show()
    }

    private fun getCalendarEvents(date: Long){
        Log.i("Date",""+date)
        val formatter = SimpleDateFormat( "dd/MM/yyyy hh:mm:ss.SSS")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        Log.i("DateTIME",""+formatter.format(calendar.time))
        var reminders = Constant.remindME.getCalendarEvent(this,date)
        setReminders(reminders)
    }

    private fun setReminders(reminders: List<CEData>) {
        rcvCalendarEvents.layoutManager = LinearLayoutManager(this)
        rcvCalendarEvents.adapter = CeAdapter(reminders, this,this)
    }

    override fun onReminderSuccess(reminderResult: ReminderResult) {
        runOnUiThread {
            Toast.makeText(this, ""+reminderResult.message, Toast.LENGTH_SHORT).show()
            getCalendarEvents(mSelectedDateMillisecond!!)
        }

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

    override fun deleteClickListner(data : Any) {
        val deleted = Constant.remindME.deleteCalendarEvent(this,data as CEData)
        Log.i("deleted",""+deleted);
        getCalendarEvents(mSelectedDateMillisecond!!)
    }

    override fun itemClickListner(data : Any) {
        ceData=data as CEData
        showBottomSheetDialog(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}