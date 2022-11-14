package com.example.familygpstracker.utility

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

class LocationUtility {

    companion object {
        public fun isLocationEnabled(context: Context) : Boolean{
            var locationManager = context.getSystemService(Context.LOCATION_SERVICE)
            var providerEnabled = (locationManager as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
            if(providerEnabled){
                return true;
            }

            return false;
        }
    }
}