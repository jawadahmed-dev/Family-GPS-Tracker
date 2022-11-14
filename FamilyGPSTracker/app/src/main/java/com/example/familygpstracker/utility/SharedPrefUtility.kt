package com.example.familygpstracker.utility

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtility(private val context: Context?)  {

    // Shared Preferences
    var pref: SharedPreferences? = null

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null

    // Context
    var _context: Context? = null

    // Shared pref mode
    var PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "UserPref"

    // Parent's child Id Key
    private val KEY_PARENT_CHILD_ID = "Parent_ChildID"

    // Child Pairing Code
    private val KEY_CHILD_PAIRING_CODE = "Child_Pairing_Code"

    init {
        _context = context
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    fun storeParentChildId(childID:String){
        editor?.putString(KEY_PARENT_CHILD_ID,childID)
        editor?.commit()
    }

    fun storePairingCode(code:String){
        editor?.putString(KEY_CHILD_PAIRING_CODE,code)
        editor?.commit()
    }

    fun getPairingCode() : String? {
        return pref!!.getString(KEY_CHILD_PAIRING_CODE, "")
    }

    fun getParentChildId() : String? {
        return pref!!.getString(KEY_PARENT_CHILD_ID, "")
    }
}