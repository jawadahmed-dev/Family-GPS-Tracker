package com.example.familygpstracker.adapter

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.familygpstracker.R
import com.example.familygpstracker.databinding.ItemLocationHistoryBinding
import com.example.familygpstracker.models.Location
import java.util.*

class LocationHistoryListAdapter(private var locationList : List<Location>, private val context: Context
) : RecyclerView.Adapter<LocationHistoryListAdapter.LocationHistoryHolder>() {

    class LocationHistoryHolder(val binding: ItemLocationHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHistoryHolder {
        val binding = DataBindingUtil.inflate<ItemLocationHistoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_location_history, parent, false)
        return LocationHistoryHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationHistoryHolder, position: Int) {
        holder.binding.childName.text = locationList[position].childName
        holder.binding.time.text = locationList[position].time
        bindAddress(holder,locationList[position])
    }

    private fun bindAddress(holder: LocationHistoryHolder,location: Location) {
        val geocoder = Geocoder(context, Locale.getDefault())
        var address = geocoder.getFromLocation(location.latitude,location.longitude,3).get(2)
        holder.binding.address.text = "${address.featureName}, ${address.subLocality}, ${address.locality}, ${address.adminArea}, ${address.countryName}"

    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    public fun setLocations(locationList : List<Location>){
        this.locationList = locationList
        notifyDataSetChanged()
    }
}