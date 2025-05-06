package org.intelehealth.app.ui.patient.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ViewPatientInfoTabBinding
import org.intelehealth.app.utility.LanguageUtils
import org.intelehealth.common.extensions.applyLabelAsScreenTitle
import org.intelehealth.common.ui.fragment.ChangePhotoFragment
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.config.room.entity.ActiveFeatureStatus
import org.intelehealth.config.utility.PatientInfoGroup
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-04-2025 - 19:53.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
abstract class PatientInfoTabFragment(@LayoutRes layoutResId: Int) : ChangePhotoFragment(layoutResId) {
    private lateinit var tabBinding: ViewPatientInfoTabBinding
    private val activeFeatureViewModel: ActiveFeatureStatusViewModel by viewModels()

    @Inject
    lateinit var languageUtils: LanguageUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabBinding = getPatientInfoTabBinding()
        observeActiveFeatureStatus()
        applyLabelAsScreenTitle()
    }

    private fun observeActiveFeatureStatus() {
        activeFeatureViewModel.fetchActiveFeatureStatus().observe(viewLifecycleOwner) { featureStatus ->
            featureStatus?.let { updatePatientInfoTabVisibility(it) }
        }
    }

    private fun updatePatientInfoTabVisibility(status: ActiveFeatureStatus) {
        if (status.activeStatusPatientOther.not() && status.activeStatusPatientAddress.not()) {
            tabBinding.root.isVisible = false
        } else {
            tabBinding.root.isVisible = true
            tabBinding.activeAddress = status.activeStatusPatientAddress
            tabBinding.activeOther = status.activeStatusPatientOther
        }
    }

    open fun changeIconStatus(stage: PatientInfoGroup) {
        when (stage) {
            PatientInfoGroup.PERSONAL -> changePersonalIconStatus()
            PatientInfoGroup.ADDRESS -> changeAddressIconStatus()
            PatientInfoGroup.OTHER -> changeOtherIconStatus()
        }
    }

    private fun changePersonalIconStatus() {
        tabBinding.tvIndicatorPatientPersonal.isSelected = true
    }

    private fun changeAddressIconStatus() {
        tabBinding.tvIndicatorPatientPersonal.isActivated = true
        tabBinding.tvIndicatorPatientAddress.isSelected = true
    }

    private fun changeOtherIconStatus() {
        tabBinding.tvIndicatorPatientPersonal.isActivated = true
        tabBinding.tvIndicatorPatientAddress.isActivated = true
        tabBinding.tvIndicatorPatientOther.isSelected = true
    }

    abstract fun getPatientInfoTabBinding(): ViewPatientInfoTabBinding

    override fun onProfilePictureSelected(uri: Uri) {
        Timber.d { "Selected Image URI: $uri" }
    }

    private fun setScreenTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }
}
