package org.intelehealth.app.ui.patient.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityFindPatientBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel

/**
 * Created by Vaghela Mithun R. on 22-06-2025 - 17:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class FindPatientActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivityFindPatientBinding
    private val afsViewModel: ActiveFeatureStatusViewModel by viewModels()

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphFindPatient) as NavHostFragment
        navHostFragment.navController
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String =
        navController.currentDestination?.label?.let {
            toString()
        } ?: ""

    private fun observeActiveFeatureStatus() {
        afsViewModel.fetchActiveFeatureStatus().observe(this) { status ->
            Timber.d { "Active feature status: $status" }
        }
    }

    /**
     * This is the main entry point for the Patient Registration Activity.
     * It sets up the layout and initializes any necessary components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appBarLayout.toolbar.contentInsetStartWithNavigation = 0
    }
}
