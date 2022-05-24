package com.example.familygpstracker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.familygpstracker.R
import com.example.familygpstracker.activities.ParentActivity
import com.example.familygpstracker.databinding.ItemFamilyMemberBinding
import com.example.familygpstracker.models.Child
import com.example.familygpstracker.utility.SharedPrefUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.Handler


class ChildListAdapter(private var listOfChild : List<Child>, private val context: Context) :
    RecyclerView.Adapter<ChildListAdapter.ChildHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        val binding = DataBindingUtil.inflate<ItemFamilyMemberBinding>(LayoutInflater.from(parent.context),R.layout.item_family_member, parent, false)
        return ChildHolder(binding)
    }


    override fun onBindViewHolder(holder: ChildHolder, position: Int) {

        bindData(holder.binding,position)
        changeLayoutColor(holder,position)

        registerListener(holder,position)

    }

    private fun updateSelectedUser(childId:String,position: Int) {
            var list = ArrayList<String>()
            list.add(0,childId)
            list.add(1,position.toString())
            //(context as ParentActivity).mainViewModel.updateSelectedChildInfo(list)

    }

    private fun bindData(binding: ItemFamilyMemberBinding, position: Int) {
        binding.familyMemberName.text = listOfChild[position].name
    }


    private fun changeLayoutColor(holder: ChildHolder, position: Int) {

        if((context as ParentActivity).mainViewModel.lastIndex.value == position){
            holder.binding.familyMemberImageView.setImageResource(R.drawable.coloured_man)
            holder.binding.familyMemberName.setTextColor(ContextCompat.getColor(context, R.color.light_cyan))
            holder.binding.circleBackground.setBackgroundResource(R.drawable.shape_circle_light_cyan)

        }
        else {
            holder.binding.familyMemberImageView.setImageResource(R.drawable.man)
            holder.binding.familyMemberName.setTextColor(ContextCompat.getColor(context, R.color.grey))
            holder.binding.circleBackground.setBackgroundResource(R.drawable.shape_circle_light_gray)
        }
    }

    private fun registerListener(holder: ChildHolder, position: Int) {
        holder.itemView.setOnClickListener({

            if((context as ParentActivity).mainViewModel.lastIndex.value != position){
                (context as ParentActivity).mainViewModel.updatelastIndex(position)
                (context as ParentActivity).mainViewModel.updateSelectedChildId(listOfChild[position].childId)
                //updateSelectedUser(listOfChild[position].childId,position)
                //SharedPrefUtility(context).storeParentChildId(listOfChild[position].childId)

                CoroutineScope(Dispatchers.IO).launch {
                    (context as ParentActivity).mainViewModel.getLastTenLocations(listOfChild[position].childId)
                    (context as ParentActivity).mainViewModel.getLastLocation(listOfChild[position].childId)
                    (context as ParentActivity).mainViewModel.getGeofenceList(listOfChild[position].childId)

                }
                notifyDataSetChanged()
            }

        })
    }

    override fun getItemCount(): Int {
        return listOfChild.size
    }

    /*fun setLastIndex(lastIndex:Int){
        this.lastIndex = lastIndex
        notifyDataSetChanged()
    }*/

    fun setChildrens(children: List<Child>) {
        listOfChild = children
        notifyDataSetChanged()
    }



    class ChildHolder(val binding: ItemFamilyMemberBinding) : RecyclerView.ViewHolder(binding.root)
}