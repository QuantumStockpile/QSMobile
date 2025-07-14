package com.quantstock.qsmobile.api

import android.content.Context
import androidx.core.content.edit

object TokenStorage {
    private const val QS_AUTH_PREFS = "auth_prefs"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"

    fun saveTokens(context: Context, accessToken: String, refreshToken: String?) {
        val prefs = context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
            .getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(context: Context): String? {
        return context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
            .getString(REFRESH_TOKEN, null)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE).edit { clear() }
    }
}