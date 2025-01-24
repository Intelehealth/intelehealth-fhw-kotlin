package org.intelehealth.app.ui.home.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentHomeBinding
import org.intelehealth.app.ui.home.viewmodel.HomeViewModel
import org.intelehealth.common.ui.fragment.MenuFragment

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 17:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class HomeFragment : MenuFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        updatePrescriptionStatus()
        updateFollowUpStatus()
        updateAppointmentStatus()
    }

    private fun updatePrescriptionStatus() {
        homeViewModel.getVisitStatusCount().observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.presCount = it
        }
    }

    private fun updateFollowUpStatus() {
        homeViewModel.getFollowUpStatusCount().observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.followUpCount = it
        }
    }

    private fun updateAppointmentStatus() {
        homeViewModel.getAppointmentStatusCount().observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.appointmentCount = it
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_home, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}