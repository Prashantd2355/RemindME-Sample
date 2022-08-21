package com.pdhameliya.remindme_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdhameliya.remindme_sample.adapter.CrAdapter
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.CRData
import kotlinx.android.synthetic.main.activity_phone_call_reminder.*

class PhoneCallReminderActivity : AppCompatActivity(), ReminderListListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_call_reminder)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.phone_call_reminder)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        main()
    }

    private fun main() {
        fbAddCallReminder.setOnClickListener {
            startActivity(Intent(this, AddCallReminderActivity::class.java).putExtra("isUpdate",false))
        }
    }

    override fun onResume() {
        super.onResume()
        getReminders()
    }

    private fun getReminders() {
        var reminders = Constant.remindME.getCallReminderList(this)
        setReminders(reminders)
    }

    private fun setReminders(reminders: List<CRData>) {
        rcvCallReminders.layoutManager = LinearLayoutManager(this)
        rcvCallReminders.adapter = CrAdapter(reminders, this,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun deleteClickListner(data : Any) {
        val deleted = Constant.remindME.deleteCallReminder(this,data as CRData)
        Log.i("deleted",""+deleted);
        getReminders();
    }

    override fun itemClickListner(data : Any) {
        Constant.crData=data as CRData
        startActivity(Intent(this, AddCallReminderActivity::class.java).putExtra("isUpdate",true))
    }
}