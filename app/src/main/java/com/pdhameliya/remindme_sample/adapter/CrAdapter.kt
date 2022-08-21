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
import com.pdhameliya.remindmelibrary.helper.CRData
import kotlinx.android.synthetic.main.cr_item.view.*
import kotlinx.android.synthetic.main.cr_item.view.tvCall
import kotlinx.android.synthetic.main.cr_item.view.tvContactName
import kotlinx.android.synthetic.main.cr_item.view.tvContactNumber
import kotlinx.android.synthetic.main.lbr_item.view.ivAlert
import kotlinx.android.synthetic.main.lbr_item.view.ivNotification
import kotlinx.android.synthetic.main.lbr_item.view.ivSound

class CrAdapter (private val items : List<CRData>, val context: Context,
                 private val reminderListListner: ReminderListListner
) : RecyclerView.Adapter<CrAdapter.ViewHolder>() {
    class ViewHolder  (ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvCall: TextView = ItemView.tvCall
        val tvContactName: TextView = ItemView.tvContactName
        val tvContactNumber: TextView = ItemView.tvContactNumber
        val tvCallStatus: TextView = ItemView.tvCallStatus
        val ivNotification: ImageView = ItemView.ivNotification
        val ivAlert: ImageView = ItemView.ivAlert
        val ivSound: ImageView = ItemView.ivSound
        val cardCR: CardView = ItemView.cardCR
        val ivDelete: ImageView = ItemView.ivCRDelete
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cr_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvContactName.text = items.get(position).CRName
        holder.tvContactNumber.text = items.get(position).CRMobNo
        holder.tvCall.setOnClickListener{
            val u: Uri = Uri.parse("tel:" + items.get(position).CRMobNo)
            val i = Intent(Intent.ACTION_DIAL, u)

            try {
                context.startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                    .show()
            }
        }
        if(items.get(position).CRFinished == true){
            holder.tvCallStatus.setBackgroundResource(R.drawable.ic_done_status_bg)
            holder.tvCallStatus.text = "Finished"
        }else{
            holder.tvCallStatus.setBackgroundResource(R.drawable.ic_active_status_bg)
            holder.tvCallStatus.text = "Active"
        }
        if(items.get(position).CRIsNotification==true)
            holder.ivNotification.setImageResource(R.drawable.ic_notification_on)
        else
            holder.ivNotification.setImageResource(R.drawable.ic_notification_off)
        if(items.get(position).CRIsAlert==true)
            holder.ivAlert.setImageResource(R.drawable.ic_alert_on)
        else
            holder.ivAlert.setImageResource(R.drawable.ic_alert_off)
        if(items.get(position).CRIsSound)
            holder.ivSound.setImageResource(R.drawable.ic_sound_on)
        else
            holder.ivSound.setImageResource(R.drawable.ic_sound_off)

        holder.cardCR.setOnClickListener{
            reminderListListner.itemClickListner(items.get(position))
        }
        holder.ivDelete.setOnClickListener{
            reminderListListner.deleteClickListner(items.get(position))
        }
    }
}