package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var alarmReceiver: DailyReminder
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        alarmReceiver = DailyReminder()
        //TODO 10 : Update theme based on value in ListPreference
        val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val newThemeValue = newValue.toString()

            val nightMode = when (newThemeValue) {
                getString(R.string.pref_dark_auto) -> NightMode.AUTO
                getString(R.string.pref_dark_on) -> NightMode.ON
                getString(R.string.pref_dark_off) -> NightMode.OFF
                else -> NightMode.AUTO
            }

            updateTheme(nightMode.value)
            true
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))

        prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue == true) {
                alarmReceiver.setDailyReminder(requireContext())
                Toast.makeText(requireContext(), "Reminder: on", Toast.LENGTH_SHORT).show()
            } else {
                alarmReceiver.cancelAlarm(requireContext())
                Toast.makeText(requireContext(), "Reminder: off", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}