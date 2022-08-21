package com.pdhameliya.remindme_sample.interfaces

import com.pdhameliya.remindme_sample.helper.TypeObject
import com.pdhameliya.remindmelibrary.helper.LBRData

interface ReminderListListner {
    fun deleteClickListner(data : Any)
    fun itemClickListner(data : Any)
}