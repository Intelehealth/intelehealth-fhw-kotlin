package org.intelehealth.app.ui.prescription.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityPrescriptionBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity

@AndroidEntryPoint
class PrescriptionActivity : SimpleAppBarActivity() {
    private val binding by lazy { ActivityPrescriptionBinding.inflate(layoutInflater) }

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navPrescription) as NavHostFragment
        navHostFragment.navController
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.prescriptions)

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
            binding.appBarLayout.toolbar.title = destination.label
        }
    }
}