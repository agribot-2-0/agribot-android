package eu.agribot.agibotapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent

class MainActivity : Activity() {

    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
