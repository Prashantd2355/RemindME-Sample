package com.pdhameliya.remindme_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdhameliya.remindme_sample.adapter.CbAdapter
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.CBData
import kotlinx.android.synthetic.main.activity_contact_book.*

class ContactBookActivity : AppCompatActivity(), ReminderListListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_book)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.contact_book)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        main()
    }

    private fun main() {
        fbAddContact.setOnClickListener {
            startActivity(Intent(this, AddContactActivity::class.java).putExtra("isUpdate",false))
        }
    }

    override fun onResume() {
        super.onResume()
        getContacts();
    }

    private fun getContacts() {
        var contacts = Constant.remindME.getContactBookList(this)
        setContacts(contacts)
    }

    private fun setContacts(contacts: List<CBData>) {
        rcvContactBook.layoutManager = LinearLayoutManager(this)
        rcvContactBook.adapter = CbAdapter(contacts, this,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun deleteClickListner(data : Any) {
        val deleted = Constant.remindME.deleteContact(this,data as CBData)
        Log.i("deleted",""+deleted);
        getContacts();
    }

    override fun itemClickListner(data : Any) {
        Constant.cbData=data as CBData
        startActivity(Intent(this, AddContactActivity::class.java).putExtra("isUpdate",true))
    }
}