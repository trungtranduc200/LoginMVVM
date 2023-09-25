package com.example.loginmvvm.util

import android.text.TextUtils

object UtilHelper {
    fun checkIsBearerToken(mToken: String): String {
        return if (!TextUtils.isEmpty(mToken) && !mToken.startsWith("Bearer")) {
            "Bearer $mToken"
        } else mToken
    }
}