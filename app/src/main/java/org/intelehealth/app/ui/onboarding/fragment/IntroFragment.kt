package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentIntroBinding
import org.intelehealth.app.ui.onboarding.adapter.IntroPagerAdapter
import org.intelehealth.app.ui.onboarding.viewmodel.SPLASH_DELAY_TIME
import org.intelehealth.resource.R as ResourceR


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

    private fun setupAutoSlider() {
        Handler(Looper.getMainLooper()).postDelayed({
            val itemCount = binding.introPager.adapter?.itemCount ?: 0
            val currentPage = binding.introPager.currentItem
            if (currentPage < itemCount - 1) binding.introPager.setCurrentItem(currentPage + 1, true)
        }, SLIDER_INTERVAL_TIME)
    }

    private fun bindPager() {
        binding.introPager.adapter = IntroPagerAdapter(requireActivity())
        binding.introPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatedSelectedTabMargin(position)
                val itemCount = binding.introPager.adapter?.itemCount ?: 0
                binding.btnSkip.apply {
                    var btnLabel = getString(ResourceR.string.action_skip)
                    if (position == itemCount - 1) btnLabel = getString(ResourceR.string.action_done)
                    if (text.toString() != getString(ResourceR.string.action_done)) {
                        text = btnLabel
                        setupAutoSlider()
                    }
                }
            }
        })
    }

    private fun generatePagerIndicator() {
        val pagerIndicator = binding.pagerIndicator
        TabLayoutMediator(pagerIndicator, binding.introPager) { _, _ -> }.attach()
    }

    private fun updatedSelectedTabMargin(currentTab: Int) {
        val dimen = resources.getDimensionPixelSize(ResourceR.dimen.std_16dp)
        binding.pagerIndicator.post {
            for (i in 0 until binding.pagerIndicator.tabCount) {
                val tab = (binding.pagerIndicator.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as MarginLayoutParams
                if (i == currentTab) p.setMargins(dimen, 0, dimen, 0)
                else p.setMargins(0, 0, 0, 0)
                tab.requestLayout()
            }
        }
    }

    companion object {
        const val SLIDER_INTERVAL_TIME = 3000L
    }
}