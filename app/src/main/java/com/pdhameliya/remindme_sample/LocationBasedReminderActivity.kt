package com.pdhameliya.remindme_sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdhameliya.remindme_sample.adapter.LbrAdapter
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.LBRData
import kotlinx.android.synthetic.main.activity_location_based_reminder.*

class LocationBasedReminderActivity : AppCompatActivity(),ReminderListListner {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_based_reminder)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.location_based_reminder)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        main()
    }

    private fun main() {
        fbAddReminder.setOnClickListener {
            startActivity(Intent(this, AddLocationBasedReminderActivity::class.java).putExtra("isUpdate",false))
        }
    }

    override fun onResume() {
        super.onResume()
        getReminderData();
    }

    private fun getReminderData() {
        var reminders = Constant.remindME.getLocationBasedReminderList(this)
        setReminders(reminders)
    }

    private fun setReminders(reminders: List<LBRData>) {
        rcvLocationReminders.layoutManager = LinearLayoutManager(this)
        rcvLocationReminders.adapter = LbrAdapter(reminders, this,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun deleteClickListner(data : Any) {
       val deleted = Constant.remindME.deleteLocationBasedReminder(this,data as LBRData)
        Log.i("deleted",""+deleted);
        getReminderData();
    }

    override fun itemClickListner(data : Any) {
        Constant.lbrData=data as LBRData
        startActivity(Intent(this, AddLocationBasedReminderActivity::class.java).putExtra("isUpdate",true))
    }
}