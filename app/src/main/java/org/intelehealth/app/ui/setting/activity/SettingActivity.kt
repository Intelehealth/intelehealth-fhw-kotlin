package org.intelehealth.app.ui.setting.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivitySettingBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 18-03-2025 - 12:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SettingActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivitySettingBinding

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navSetting) as NavHostFragment
        navHostFragment.navController
    }

    // AppBarConfiguration for the activity
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActionBarSet() {
        super.onActionBarSet()
        setupNavigation()
    }

    /**
     * Setup navigation controller with toolbar
     */
    private fun setupNavigation() {
        // Create an AppBarConfiguration with the correct top-level destinations
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        val toolbar = binding.appBarLayout.toolbar
        // Hook your navigation controller to toolbar
//        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Set the title of the screen
            binding.appBarLayout.toolbar.title = destination.label
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(ResourceR.string.action_settings)
}
