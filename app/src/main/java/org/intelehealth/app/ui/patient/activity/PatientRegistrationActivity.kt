package org.intelehealth.app.ui.patient.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityPatientRegistrationBinding
import org.intelehealth.app.model.ConsentArgs
import org.intelehealth.app.ui.patient.fragment.PatientConsentFragmentArgs
import org.intelehealth.app.ui.patient.fragment.PatientPersonalInfoFragmentArgs
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.room.entity.ActiveFeatureStatus

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 13:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PatientRegistrationActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivityPatientRegistrationBinding
    private val afsViewModel: ActiveFeatureStatusViewModel by viewModels()

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphPatient) as NavHostFragment
        navHostFragment.navController
    }

    private val navGraph by lazy {
        navController.navInflater.inflate(R.navigation.patient_navigation_graph)
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String =
        navController.currentDestination?.label?.let {
            toString()
        } ?: ""

    private fun observeActiveFeatureStatus() {
        afsViewModel.fetchActiveFeatureStatus().observe(this) { status ->
            Timber.d { "Active feature status: $status" }
            setNavigationStartDestination(status)
        }
    }

    /**
     * This is the main entry point for the Patient Registration Activity.
     * It sets up the layout and initializes any necessary components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeActiveFeatureStatus()
    }

    private fun setNavigationStartDestination(status: ActiveFeatureStatus) {
        val consentArgs: ConsentArgs? = when {
            status.personalDataConsent -> ConsentArgs(ConsentArgs.ConsentType.PERSONAL_DATA_CONSENT, null, null)
            status.telemedicineConsent -> ConsentArgs(ConsentArgs.ConsentType.TELECONSULTATION, null, null)
            else -> null
        }

        consentArgs?.let {
            navGraph.setStartDestination(R.id.fragmentPatientConsent)
            navController.setGraph(navGraph, PatientConsentFragmentArgs(it).toBundle())
        } ?: run {
            Timber.d { "No active feature status found, setting default start destination." }
            navGraph.setStartDestination(R.id.fragmentPatientPersonalInfo)
            navController.setGraph(navGraph, PatientPersonalInfoFragmentArgs(null).toBundle())
        }
    }
}
