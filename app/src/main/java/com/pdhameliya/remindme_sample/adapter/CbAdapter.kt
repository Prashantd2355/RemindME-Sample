package com.pdhameliya.remindme_sample.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pdhameliya.remindme_sample.R
import com.pdhameliya.remindme_sample.interfaces.ReminderListListner
import com.pdhameliya.remindmelibrary.helper.CBData
import kotlinx.android.synthetic.main.cb_item.view.*
import kotlinx.android.synthetic.main.cb_item.view.ivAlert
import kotlinx.android.synthetic.main.cb_item.view.ivNotification
import kotlinx.android.synthetic.main.cb_item.view.ivSound


class CbAdapter (private val items : List<CBData>, val context: Context,
                 private val reminderListListner: ReminderListListner
) : RecyclerView.Adapter<CbAdapter.ViewHolder>() {
    class ViewHolder  (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvCall: TextView = ItemView.tvCall
        val tvContactTitle: TextView = ItemView.tvContactTitle
        val tvContactDesc: TextView = ItemView.tvContactDesc
        val tvContactName: TextView = ItemView.tvContactName
        val tvContactNumber: TextView = ItemView.tvContactNumber
        val tvVisits: TextView = ItemView.tvVisits
        val ivNotification: ImageView = ItemView.ivNotification
        val ivAlert: ImageView = ItemView.ivAlert
        val ivSound: ImageView = ItemView.ivSound
        val cardCB: CardView = ItemView.cardCB
        val ivDelete: ImageView = ItemView.ivCBDelete
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cb_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvContactTitle.text = items.get(position).CBTitle
        holder.tvContactDesc.text = items.get(position).CBDesc
        holder.tvContactName.text = items.get(position).CBName
        holder.tvContactNumber.text = items.get(position).CBMobNo
        holder.tvVisits.text = items.get(position).CBNoOfVisit.toString()
        holder.tvCall.setOnClickListener{
            val u: Uri = Uri.parse("tel:" + items.get(position).CBMobNo)
            val i = Intent(Intent.ACTION_DIAL, u)

            try {
                context.startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                    .show()
            }
        }
        if(items.get(position).CBIsNotification==true)
            holder.ivNotification.setImageResource(R.drawable.ic_notification_on)
        else
            holder.ivNotification.setImageResource(R.drawable.ic_notification_off)
        if(items.get(position).CBIsAlert==true)
            holder.ivAlert.setImageResource(R.drawable.ic_alert_on)
        else
            holder.ivAlert.setImageResource(R.drawable.ic_alert_off)
        if(items.get(position).CBIsSound)
            holder.ivSound.setImageResource(R.drawable.ic_sound_on)
        else
            holder.ivSound.setImageResource(R.drawable.ic_sound_off)

        holder.cardCB.setOnClickListener{
            reminderListListner.itemClickListner(items.get(position))
        }
        holder.ivDelete.setOnClickListener{
            reminderListListner.deleteClickListner(items.get(position))
        }
    }
}