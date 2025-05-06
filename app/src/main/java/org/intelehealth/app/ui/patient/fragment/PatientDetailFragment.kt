package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.common.extensions.applyLabelAsScreenTitle

/**
 * Created by Vaghela Mithun R. on 05-05-2025 - 16:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class PatientDetailFragment : Fragment(R.layout.fragment_patient_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyLabelAsScreenTitle()
    }
}
