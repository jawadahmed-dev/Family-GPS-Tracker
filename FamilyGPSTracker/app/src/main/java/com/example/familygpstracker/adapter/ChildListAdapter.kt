package com.example.familygpstracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.familygpstracker.R
import com.example.familygpstracker.databinding.ItemFamilyMemberBinding

class ChildListAdapter(private val listOfChild : List<String>,private val context:Context) :
    RecyclerView.Adapter<ChildListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemFamilyMemberBinding>(LayoutInflater.from(parent.context),R.layout.item_family_member, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener({

        })
    }

    override fun getItemCount(): Int {
        return listOfChild.size
    }

    class ViewHolder(binding: ItemFamilyMemberBinding) : RecyclerView.ViewHolder(binding.root)
}