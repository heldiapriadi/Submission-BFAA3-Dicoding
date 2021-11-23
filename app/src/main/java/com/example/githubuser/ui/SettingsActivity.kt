package com.example.githubuser.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.example.githubuser.R
import com.example.githubuser.data.remote.SettingPreferences
import com.example.githubuser.databinding.SettingsActivityBinding
import com.example.githubuser.helper.SettingViewModelFactory
import com.example.githubuser.viewmodels.SettingViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var settingViewModel: SettingViewModel
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private lateinit var darkMode: String

        private lateinit var darkModeSwitchPreference: SwitchPreferenceCompat

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pref = SettingPreferences.getInstance(requireActivity().dataStore)
            settingViewModel =
                ViewModelProvider(this, SettingViewModelFactory(pref)).get(SettingViewModel::class.java)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            init()
            subcribeUI()
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun init(){
            this.darkMode = resources.getString(R.string.pref_dark_mode)
            darkModeSwitchPreference = findPreference<SwitchPreferenceCompat>(this.darkMode) as SwitchPreferenceCompat
        }

        private fun subcribeUI(){
            settingViewModel.getThemeSettings().observe(viewLifecycleOwner, {
                setDarkMode(it)
            })
        }


        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if(this.darkMode == key){
                settingViewModel.saveThemeSetting(darkModeSwitchPreference.isChecked)
            }
        }

        private fun setDarkMode(isON: Boolean){
            darkModeSwitchPreference.isChecked = isON
            if(isON){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}