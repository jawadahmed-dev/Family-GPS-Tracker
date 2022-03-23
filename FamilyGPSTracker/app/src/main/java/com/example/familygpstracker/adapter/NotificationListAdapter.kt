package com.example.familygpstracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.familygpstracker.R
import com.example.familygpstracker.databinding.ItemFamilyMemberBinding
import com.example.familygpstracker.databinding.ItemNotificationBinding
import com.example.familygpstracker.models.Notification

class NotificationListAdapter(private var notificationList : List<Notification>, private val context: Context
) : RecyclerView.Adapter<NotificationListAdapter.NotificationHolder>() {

    class NotificationHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val binding = DataBindingUtil.inflate<ItemNotificationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_notification, parent, false)
        return NotificationHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.binding.notificationMessage.text = notificationList[position].message
        holder.binding.createdDate.text = notificationList[position].createdAt
    }

    override fun getItemCount(): Int {
       return notificationList.size
    }

    public fun setNotifications(notificationList : List<Notification>){
        this.notificationList = notificationList
        notifyDataSetChanged()
    }
}