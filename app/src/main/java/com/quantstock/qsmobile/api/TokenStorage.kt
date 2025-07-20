package com.quantstock.qsmobile.api

import android.content.Context
import androidx.core.content.edit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

private const val QS_AUTH_PREFS = "auth_prefs"
private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"

class TokenStorage @Inject constructor(@ApplicationContext private val context: Context){
    fun saveTokens(accessToken: String, refreshToken: String?) {
        val prefs = context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(): String? {
        return context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
            .getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE)
            .getString(REFRESH_TOKEN, null)
    }

    fun clear() {
        context.getSharedPreferences(QS_AUTH_PREFS, Context.MODE_PRIVATE).edit { clear() }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return TokenStorage(context)
    }
}