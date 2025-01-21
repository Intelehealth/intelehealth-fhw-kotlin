package org.intelehealth.app.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentHomeBinding
import org.intelehealth.app.ui.home.viewmodel.HomeViewModel

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 17:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by lazy { FragmentHomeBinding.bind(requireView()) }
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchVisitStatusCount()
    }

    private fun fetchVisitStatusCount() {
        homeViewModel.getVisitStatusCount().observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.statusCount = it
        }
    }
}