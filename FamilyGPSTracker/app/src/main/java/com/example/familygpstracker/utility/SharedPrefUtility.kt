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

    init {
        _context = context
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    fun storeParentChildId(childID:String){
        editor?.putString(KEY_PARENT_CHILD_ID,childID)
        editor?.commit()
    }

    fun getParentChildId() : String? {
        return pref!!.getString(KEY_PARENT_CHILD_ID, "")
    }
}