package com.example.familygpstracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familygpstracker.R

class ChildListAdapter(private val listOfChild : List<String>,private val context:Context) :
    RecyclerView.Adapter<ChildListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_family_member, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener({

        })
    }

    override fun getItemCount(): Int {
        return listOfChild.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}