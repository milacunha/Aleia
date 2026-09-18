package br.com.camilacunha.aleia.data.local.seed

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SeedPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isSeedDone(): Boolean = prefs.getBoolean(KEY_SEED_DONE, false)

    fun markSeedDone() {
        prefs.edit { putBoolean(KEY_SEED_DONE, true) }
    }

    private companion object {
        const val PREFS_NAME = "aleia_prefs"
        const val KEY_SEED_DONE = "seed_done"
    }
}