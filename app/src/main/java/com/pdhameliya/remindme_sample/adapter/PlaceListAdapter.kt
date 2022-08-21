package com.pdhameliya.remindme_sample.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdhameliya.remindme_sample.R
import com.pdhameliya.remindme_sample.helper.TypeObject
import com.pdhameliya.remindme_sample.interfaces.PlaceListListner
import kotlinx.android.synthetic.main.place_item.view.*

class PlaceListAdapter(private val placeList: Array<TypeObject>,private val placeListListner: PlaceListListner)
    : RecyclerView.Adapter<PlaceListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_item,parent,false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivPlaceIcon.setImageResource(placeList[position].icon!!)
        holder.tvReminderTitle.text = placeList[position].name

        holder.lyPlaceItem.setOnClickListener{
            placeListListner.placeListClickListner(placeList[position])
        }

    }


    override fun getItemCount(): Int {
        return placeList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lyPlaceItem: LinearLayout = itemView.lyPlaceItem
        val ivPlaceIcon: ImageView = itemView.ivPlaceIcon
        val tvReminderTitle: TextView = itemView.tvReminderTitle
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
}