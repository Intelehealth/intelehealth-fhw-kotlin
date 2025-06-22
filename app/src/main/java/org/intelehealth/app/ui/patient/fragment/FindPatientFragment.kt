package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientListBinding
import org.intelehealth.app.ui.patient.viewmodel.PatientDetailViewModel
import org.intelehealth.common.ui.fragment.BaseProgressFragment

/**
 * Created by Vaghela Mithun R. on 27-05-2025 - 12:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class FindPatientFragment : BaseProgressFragment(R.layout.fragment_patient_list) {

    override val viewModel: PatientDetailViewModel by viewModels()
    private lateinit var binding: FragmentPatientListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPatientListBinding.bind(view)
        bindProgressView(binding.progressView)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}
