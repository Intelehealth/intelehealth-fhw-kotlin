package org.intelehealth.app.ui.patient.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityPatientRegistrationBinding
import org.intelehealth.app.model.ConsentArgs
import org.intelehealth.app.ui.patient.fragment.PatientConsentFragmentArgs
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 13:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PatientRegistrationActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivityPatientRegistrationBinding
    private val regFieldViewModel: RegFieldViewModel by viewModels()

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphPatient) as NavHostFragment
        navHostFragment.navController
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = navController.currentDestination?.label.toString()

    /**
     * This is the main entry point for the Patient Registration Activity.
     * It sets up the layout and initializes any necessary components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationStartDestination()
    }

    private fun setNavigationStartDestination() {
        val consentArgs = ConsentArgs(ConsentArgs.ConsentType.PERSONAL_DATA_CONSENT, null, null)
        navController.navInflater.inflate(R.navigation.patient_navigation_graph).apply {
            this.setStartDestination(R.id.fragmentPatientConsent)
            navController.setGraph(this, PatientConsentFragmentArgs(consentArgs).toBundle())
        }
    }
}
