package com.pdhameliya.remindme_sample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pdhameliya.remindme_sample.R
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.CEData
import com.pdhameliya.remindmelibrary.helper.LocationUIHelper
import com.pdhameliya.remindmelibrary.remiders.calendarview.helper.TimeUtil
import kotlinx.android.synthetic.main.ce_item.view.*
import kotlinx.android.synthetic.main.ce_item.view.ivAlert
import kotlinx.android.synthetic.main.ce_item.view.ivNotification
import kotlinx.android.synthetic.main.ce_item.view.ivSound

class CeAdapter (private val items : List<CEData>, val context: Context,
                 private val reminderListListner: ReminderListListner
) : RecyclerView.Adapter<CeAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.ce_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEventTitle.text = items.get(position).CETitle
        holder.tvEventDesc.text = items.get(position).CEDesc
        holder.tvEventDateTime.text = TimeUtil().getDate(items.get(position).CEDate!!)+" "+TimeUtil().getTime(items.get(position).CETime!!)
        holder.tvEventNavigate.setOnClickListener{
            LocationUIHelper.navigateToMap(context, items.get(position).CELattitude!!,
                items.get(position).CELongitude!!
            );
        }
        if(items.get(position).CEIsFinished == true){
            holder.tvEventStatus.setBackgroundResource(R.drawable.ic_done_status_bg)
            holder.tvEventStatus.text = "Finished"
        }else{
            holder.tvEventStatus.setBackgroundResource(R.drawable.ic_active_status_bg)
            holder.tvEventStatus.text = "Active"
        }
        if(items.get(position).CEIsNotification==true)
            holder.ivNotification.setImageResource(R.drawable.ic_notification_on)
        else
            holder.ivNotification.setImageResource(R.drawable.ic_notification_off)
        if(items.get(position).CEIsAlert==true)
            holder.ivAlert.setImageResource(R.drawable.ic_alert_on)
        else
            holder.ivAlert.setImageResource(R.drawable.ic_alert_off)
        if(items.get(position).CEIsSound)
            holder.ivSound.setImageResource(R.drawable.ic_sound_on)
        else
            holder.ivSound.setImageResource(R.drawable.ic_sound_off)

        holder.cardCE.setOnClickListener{
            reminderListListner.itemClickListner(items.get(position))
        }
        holder.ivDelete.setOnClickListener{
            reminderListListner.deleteClickListner(items.get(position))
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvEventTitle: TextView = view.tvEventTitle
        val tvEventDesc: TextView = view.tvEventDesc
        val tvEventDateTime: TextView = view.tvEventDateTime
        val tvEventNavigate: LinearLayout = view.lyEventNavigate
        val tvEventStatus: TextView = view.tvEventStatus
        val ivNotification: ImageView = view.ivNotification
        val ivAlert: ImageView = view.ivAlert
        val ivSound: ImageView = view.ivSound
        val cardCE: CardView = view.cardCE
        val ivDelete: ImageView = view.ivCEDelete
    }
}