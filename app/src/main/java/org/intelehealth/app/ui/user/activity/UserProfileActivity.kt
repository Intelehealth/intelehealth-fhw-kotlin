package org.intelehealth.app.ui.user.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.resource.R as ResourceR
import org.intelehealth.app.databinding.ActivityUserProfileBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity

/**
 * Created by Vaghela Mithun R. on 24-03-2025 - 18:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class UserProfileActivity : SimpleAppBarActivity() {

    private val binding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navUserProfile) as NavHostFragment
        navHostFragment.navController
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(ResourceR.string.title_my_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Set the title of the screen
            binding.appBarLayout.toolbar.title = destination.label
        }
    }
}
