package com.ssafy.ourhome.utils

import android.content.SharedPreferences
import com.ssafy.data.utils.EMAIL
import com.ssafy.data.utils.FAMILY_CODE

object Prefs {
    var prefs: SharedPreferences? = null
//    private val prefs = _prefs!!

    var email: String
        get() = prefs?.getString(EMAIL, "") ?: ""
        set(value) {
            prefs?.edit()?.putString(EMAIL, value)?.apply()
        }

    var familyCode: String
        get() = prefs?.getString(FAMILY_CODE, "") ?: ""
        set(value) {
            prefs?.edit()?.putString(FAMILY_CODE, value)?.apply()
        }
}