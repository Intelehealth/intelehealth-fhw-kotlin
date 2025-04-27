package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAyuPolicyBinding
import org.intelehealth.app.databinding.FragmentPrescriptionListBinding
import org.intelehealth.app.databinding.FragmentSetupBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionPagerAdapter

class PrescriptionListFragment : Fragment(R.layout.fragment_prescription_list) {
    private lateinit var binding: FragmentPrescriptionListBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionListBinding.bind(view)
        setupTabAndViewPager()
    }

    private fun setupTabAndViewPager() {
        if (binding.prescriptionViewPager.adapter == null) {
            val adapter = PrescriptionPagerAdapter(requireActivity())
            binding.prescriptionViewPager.setAdapter(adapter)

            TabLayoutMediator(
                binding.prescriptionTabLayout, binding.prescriptionViewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.setText(
                    resources.getString(
                        if (position == 0) R.string.received else R.string.pending
                    )
                ).setIcon(org.intelehealth.resource.R.drawable.presc_tablayout_icon)
            }.attach()

            binding.prescriptionViewPager.setOffscreenPageLimit(1)
        }
    }
}