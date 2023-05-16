package com.hamzaazman.kotlinfreetoplay.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hamzaazman.kotlinfreetoplay.common.Constant.CATEGORY_ID_PREF_KEY
import com.hamzaazman.kotlinfreetoplay.common.Constant.CATEGORY_PREFERENCES
import com.hamzaazman.kotlinfreetoplay.common.Constant.CATEGORY_PREF_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


@ActivityRetainedScoped
class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferenceKeys {
        val checkedCategory = stringPreferencesKey(CATEGORY_PREF_KEY)
        val checkedCategoryId = intPreferencesKey(CATEGORY_ID_PREF_KEY)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = CATEGORY_PREFERENCES)

    suspend fun saveCategoryAndId(
        category: String, categoryId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.checkedCategory] = category
            preferences[PreferenceKeys.checkedCategoryId] = categoryId
        }
    }

    val getCategoryAndId: Flow<CategoryType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val checkedCategory = preferences[PreferenceKeys.checkedCategory] ?: "all"
            val checkedCategoryId = preferences[PreferenceKeys.checkedCategoryId] ?: 0
            CategoryType(
                checkedCategory = checkedCategory,
                checkedCategoryId = checkedCategoryId
            )
        }

}

data class CategoryType(
    val checkedCategory: String,
    val checkedCategoryId: Int,
)