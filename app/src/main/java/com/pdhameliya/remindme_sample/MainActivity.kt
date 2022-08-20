package com.pdhameliya.remindme_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pdhameliya.remindmelibrary.RemindME

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var remindME = RemindME().getInstance(this)!!
    }
}