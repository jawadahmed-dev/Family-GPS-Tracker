package com.example.familygpstracker.utility

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.example.familygpstracker.broadcastreceiver.GeofenceBroadcastReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class GeofenceUtility(val base: Context?) : ContextWrapper(base) {

    private val TAG : String = "GeofenceHelper"
    private lateinit var pendingIntent:PendingIntent

    public fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    public fun getGeofence(stringId : String, latLng: LatLng, radius:Float, transitionType:Int)
    : Geofence {

        return Geofence.Builder()
            .setCircularRegion(latLng.latitude,latLng.longitude,radius)
            .setRequestId(stringId)
            .setTransitionTypes(transitionType)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    public fun getPendingIntent(): PendingIntent {

        /*if(pendingIntent != null ){
            return pendingIntent
        }*/
        var intent = Intent(base, GeofenceBroadcastReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(this,2607, intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

        return pendingIntent
    }

    public fun getErrorString(e:Exception ) : String{
        if (e is ApiException){
            var apiException = e as ApiException
            when(apiException.statusCode){
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }
}