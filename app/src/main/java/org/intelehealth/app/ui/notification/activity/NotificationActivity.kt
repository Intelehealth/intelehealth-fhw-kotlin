package org.intelehealth.app.ui.notification.activity

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityNotificationBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity

@AndroidEntryPoint
class NotificationActivity: SimpleAppBarActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphFindNotification) as NavHostFragment
        navHostFragment.navController
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(org.intelehealth.resource.R.string.title_notification)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onActionBarSet() {
        super.onActionBarSet()
        setupNavigation()
    }

    /**
     * Sets up the navigation controller and synchronizes the toolbar title with
     * the current destination.
     */
    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Set the title of the screen
            binding.appBarLayout.toolbar.title = destination.label
        }
    }
}