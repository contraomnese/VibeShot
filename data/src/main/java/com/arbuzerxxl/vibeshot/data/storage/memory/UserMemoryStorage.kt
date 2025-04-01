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

private val ID = stringPreferencesKey("userId")
private val FULLNAME = stringPreferencesKey("fullname")
private val USERNAME = stringPreferencesKey("username")
private val TOKEN = stringPreferencesKey("token")
private val TOKEN_SECRET = stringPreferencesKey("secret")
private val AUTHENTICATED = booleanPreferencesKey("authenticated")

class UserMemoryStorage(private val context: Context) : UserStorage {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    override fun observe(): Flow<UserEntity?> {

        return context.dataStore.data.map { preferences ->
            try {
                UserEntity(
                    nsid = preferences[ID]!!,
                    fullName = preferences[FULLNAME]!!,
                    userName = preferences[USERNAME]!!,
                    token = preferences[TOKEN]!!,
                    tokenSecret = preferences[TOKEN_SECRET]!!,
                    type = if (preferences[AUTHENTICATED]!!) UserType.AUTHENTICATED else UserType.GUEST
                )
            } catch (e: NullPointerException) {
                null
            }
        }
    }

    override suspend fun save(user: UserEntity) {
        context.dataStore.edit { preferences ->
            preferences[ID] = user.nsid
            preferences[FULLNAME] = user.fullName
            preferences[USERNAME] = user.userName
            preferences[TOKEN] = user.token
            preferences[TOKEN_SECRET] = user.token
            preferences[AUTHENTICATED] = user.type == UserType.AUTHENTICATED
        }
    }

    override suspend fun delete() {
        context.dataStore.edit { preferences ->
            preferences.remove(ID)
            preferences.remove(FULLNAME)
            preferences.remove(USERNAME)
            preferences.remove(TOKEN)
            preferences.remove(TOKEN_SECRET)
            preferences.remove(AUTHENTICATED)
        }
    }
}