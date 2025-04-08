package com.arbuzerxxl.vibeshot.data.storage.memory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arbuzerxxl.vibeshot.data.storage.api.UserStorage
import com.arbuzerxxl.vibeshot.data.storage.models.UserEntity
import com.arbuzerxxl.vibeshot.data.storage.models.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

private const val GUEST = "Guest"

class UserMemoryStorage(private val context: Context) : UserStorage {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    private object PreferencesKeys {
        val ID = stringPreferencesKey("userId")
        val FULLNAME = stringPreferencesKey("fullname")
        val USERNAME = stringPreferencesKey("username")
        val TOKEN = stringPreferencesKey("token")
        val TOKEN_SECRET = stringPreferencesKey("secret")
        val AUTHENTICATED = booleanPreferencesKey("authenticated")
    }

    override val user: Flow<UserEntity?> =
        context.dataStore.data.map { preferences ->
            try {
                UserEntity(
                    nsid = preferences[PreferencesKeys.ID]!!,
                    fullName = preferences[PreferencesKeys.FULLNAME]!!,
                    userName = preferences[PreferencesKeys.USERNAME]!!,
                    token = preferences[PreferencesKeys.TOKEN],
                    tokenSecret = preferences[PreferencesKeys.TOKEN_SECRET],
                    type = if (preferences[PreferencesKeys.AUTHENTICATED]!!) UserType.AUTHENTICATED else UserType.GUEST
                )
            } catch (e: NullPointerException) {
                null
            }
        }


    override suspend fun save(user: UserEntity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ID] = user.nsid
            preferences[PreferencesKeys.FULLNAME] = user.fullName
            preferences[PreferencesKeys.USERNAME] = user.userName
            preferences[PreferencesKeys.TOKEN] = user.token!!
            preferences[PreferencesKeys.TOKEN_SECRET] = user.tokenSecret!!
            preferences[PreferencesKeys.AUTHENTICATED] = user.type == UserType.AUTHENTICATED
        }
    }

    override suspend fun saveAsGuest() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ID] = UUID.randomUUID().toString()
            preferences[PreferencesKeys.FULLNAME] = GUEST
            preferences[PreferencesKeys.USERNAME] = GUEST
            preferences[PreferencesKeys.AUTHENTICATED] = false
        }
    }

    override suspend fun delete() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ID)
            preferences.remove(PreferencesKeys.FULLNAME)
            preferences.remove(PreferencesKeys.USERNAME)
            preferences.remove(PreferencesKeys.TOKEN)
            preferences.remove(PreferencesKeys.TOKEN_SECRET)
            preferences.remove(PreferencesKeys.AUTHENTICATED)
        }
    }
}