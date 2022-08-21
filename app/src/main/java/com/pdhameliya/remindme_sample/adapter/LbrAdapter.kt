package com.pdhameliya.remindme_sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pdhameliya.remindme_sample.R
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.LBRData
import kotlinx.android.synthetic.main.lbr_item.view.*

class LbrAdapter(
    private val items: List<LBRData>,
    val context: Context,
    private val reminderListListner: ReminderListListner
) : RecyclerView.Adapter<LbrAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvReminderTitle: TextView = ItemView.tvReminderTitle
        val tvReminderDesc: TextView = ItemView.tvReminderDesc
        val tvReminderStatus: TextView = ItemView.tvReminderStatus
        val ivNotification: ImageView = ItemView.ivNotification
        val ivAlert: ImageView = ItemView.ivAlert
        val ivSound: ImageView = ItemView.ivSound
        val cardLbr: CardView = ItemView.cardLbr
        val ivDelete: ImageView = ItemView.ivDelete
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lbr_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvReminderTitle.text = items.get(position).LBRTitle
        holder.tvReminderDesc.text = items.get(position).LBRDesc
        if(items.get(position).LBRFinished == true){
            holder.tvReminderStatus.setBackgroundResource(R.drawable.ic_done_status_bg)
            holder.tvReminderStatus.text = "Finished"
        }else{
            holder.tvReminderStatus.setBackgroundResource(R.drawable.ic_active_status_bg)
            holder.tvReminderStatus.text = "Active"
        }
        if(items.get(position).LBRIsNotification==true)
            holder.ivNotification.setImageResource(R.drawable.ic_notification_on)
        else
            holder.ivNotification.setImageResource(R.drawable.ic_notification_off)
        if(items.get(position).LBRIsAlert==true)
            holder.ivAlert.setImageResource(R.drawable.ic_alert_on)
        else
            holder.ivAlert.setImageResource(R.drawable.ic_alert_off)
        if(items.get(position).LBRIsSound)
            holder.ivSound.setImageResource(R.drawable.ic_sound_on)
        else
            holder.ivSound.setImageResource(R.drawable.ic_sound_off)

        holder.cardLbr.setOnClickListener{
            reminderListListner.itemClickListner(items.get(position))
        }
        holder.ivDelete.setOnClickListener{
            reminderListListner.deleteClickListner(items.get(position))
        }
    }
}