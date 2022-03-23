package com.example.familygpstracker.utility

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.familygpstracker.activities.AuthenticationActivity
import com.example.familygpstracker.activities.ParentActivity


class SessionManager(private val context: Context?) {
    // Shared Preferences
    var pref: SharedPreferences? = null

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor? = null

    // Context
    var _context: Context? = null

    // Shared pref mode
    var PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "AndroidPref"

    // All Shared Preferences Keys
    private val IS_LOGIN = "IsLoggedIn"

    // All Shared Preferences Keys
    private val KEY_EXPIRY_TIME = "expiryTime"

    // Parent Id (make variable public to access from outside)
    val KEY_ParentId = "parentId"

    val DEVICE_TOKEN = "deviceToken"

    val KEY_CHILD_PARENT_ID = "childParentId"

    // Child Id (make variable public to access from outside)
    val KEY_ChildId = "childId"

    val KEY_UserId = "userId"

    val KEY_USER_TYPE = "userType"

    // Constructor

    init {
        _context = context
        pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    /**
     * Create login session
     */
    fun createParentLoginSession(parentId: String?, userType:String?, userId:String?) : Unit {
        // Storing login value as TRUE
        editor?.putBoolean(IS_LOGIN, true)

        // Storing name in pref
        editor?.putString(KEY_ParentId, parentId)


        // Storing userType in pref
        editor?.putString(KEY_USER_TYPE, userType)

        // Storing id in pref
        editor?.putString(KEY_UserId, userId)

        // commit changes
        editor?.commit()
    }

    fun createChildLoginSession( childId: String?, userType:String?, userId:String?) {
        // Storing login value as TRUE
        editor?.putBoolean(IS_LOGIN, true)

        // Storing email in pref
        editor?.putString(KEY_ChildId, childId)

        // Storing userType in pref
        editor?.putString(KEY_USER_TYPE, userType)

        // Storing id in pref
        editor?.putString(KEY_UserId, userId)

        // commit changes
        editor?.commit()
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
  /*  fun checkLogin() {
        // Check login status
        if (!isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context.startActivity(i)
        }
    }*/


    /**
     * Get stored session data
     */
    fun getParentId(): String? {
        return pref!!.getString(KEY_ParentId, null)
    }

    fun getChildId(): String? {
        return pref!!.getString(KEY_ChildId, null)
    }

    fun getChildParentId(): String? {
        return pref!!.getString(KEY_CHILD_PARENT_ID, null)
    }

    fun getDeviceToken(): String? {
        return pref!!.getString(DEVICE_TOKEN, null)
    }

    fun storeChildParentId(parentID:String){
        editor?.putString(KEY_CHILD_PARENT_ID,parentID)
        editor?.commit()
    }

    fun getUserType() : String? {
        return pref!!.getString(KEY_USER_TYPE, null)
    }

    fun getExpiryTime() : String? {
        return pref!!.getString(KEY_EXPIRY_TIME,null)
    }

    fun setExpiryTime(expirytime:String){
        editor?.putString(KEY_EXPIRY_TIME,expirytime)
        editor?.commit()
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor?.clear()
        editor?.commit()

        // After logout redirect user to Loing Activity
        val i = Intent(_context, AuthenticationActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login Activity
        _context?.startActivity(i)
        (_context as ParentActivity)?.finish()
    }

    fun storeToken(token:String){
        editor?.putString(DEVICE_TOKEN,token)
        editor?.commit()
    }

    /**
     * Quick check for login
     */
    // Get Login State
    fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }
}