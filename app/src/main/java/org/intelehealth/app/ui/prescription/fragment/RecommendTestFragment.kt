package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentRecommendTestBinding
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 13:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class RecommendTestFragment : Fragment(R.layout.fragment_recommend_test) {
    private lateinit var binding: FragmentRecommendTestBinding
    private val viewModel: PrescriptionDetailsViewModel by activityViewModels<PrescriptionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecommendTestBinding.bind(view)
        binding.lblTest.isSelected = true
        toggleTestInfoCardVisibility()
        observerTestDetails()
    }

    private fun toggleTestInfoCardVisibility() {
        binding.lblTest.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.testInfoInfoExpandableLayoutGroup.isVisible = it.isSelected
        }
    }

    private fun observerTestDetails() {
        viewModel.getRequestTestDetails().observe(viewLifecycleOwner) {
            it ?: return@observe
            Timber.d { "Test => $it" }
            binding.value = viewModel.formatToBulletPoints(it)
        }
    }
}
