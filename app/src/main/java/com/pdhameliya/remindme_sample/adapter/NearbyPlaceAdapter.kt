package com.pdhameliya.remindme_sample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdhameliya.remindme_sample.R
import com.pdhameliya.remindme_sample.helper.landmark.Results
import com.pdhameliya.remindmelibrary.helper.LocationUIHelper
import kotlinx.android.synthetic.main.place_result_item.view.*

class NearbyPlaceAdapter (private val items : ArrayList<Results>, val context: Context) : RecyclerView.Adapter<NearbyPlaceAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.place_result_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvPlaceName.text = items.get(position).name
        holder.tvPlaceAddress.text = items.get(position).vicinity
        holder.tvPlaceStatus.text = items.get(position).businessStatus
        holder.lyPlaceNavigate.setOnClickListener{
            LocationUIHelper.navigateToMap(context, items.get(position).geometry?.location?.lat!!,
                items.get(position).geometry?.location?.lng!!
            );
        }
        if(items.get(position).openingHours?.openNow == true){
            holder.tvIsOpen.setBackgroundResource(R.drawable.ic_done_status_bg)
            holder.tvIsOpen.text = "OPEN"
        }else{
            holder.tvIsOpen.setBackgroundResource(R.drawable.ic_active_status_bg)
            holder.tvIsOpen.text = "CLOSED"
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvPlaceName: TextView = view.tvPlaceName
        val tvPlaceAddress: TextView = view.tvPlaceAddress
        val tvPlaceStatus: TextView = view.tvPlaceStatus
        val lyPlaceNavigate: LinearLayout = view.lyPlaceNavigate
        val tvIsOpen: TextView = view.tvIsOpen
    }
}
