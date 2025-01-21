package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentIntroBinding
import org.intelehealth.app.ui.onboarding.adapter.IntroPagerAdapter

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class IntroFragment : Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBinding.bind(view)
        bindPager()
        generatePagerIndicator()
        binding.btnSkip.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroToAyuPolicy())
        }
    }

    private fun bindPager() {
        binding.introPager.adapter = IntroPagerAdapter(requireActivity())
    }

    private fun generatePagerIndicator() {
        val pagerIndicator = binding.pagerIndicator
        TabLayoutMediator(pagerIndicator, binding.introPager) { _, _ -> }.attach()
    }
}