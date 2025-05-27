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
/**
 * A fragment that displays an introduction sequence using a ViewPager2.
 *
 * This fragment presents a series of introductory slides to the user. It
 * utilizes a ViewPager2 with a custom adapter ([IntroPagerAdapter]) to manage
 * the slides. It also includes a pager indicator and a "Skip" button that
 * allows the user to navigate to the next step (e.g., a policy screen).
 */
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

    /**
     * Sets up automatic sliding between pages in the ViewPager2.
     *
     * This method uses a [Handler] to post a delayed action that advances the
     * ViewPager2 to the next page if it's not at the last page. This creates an
     * automatic slideshow effect.
     */
    private fun setupAutoSlider() {
        Handler(Looper.getMainLooper()).postDelayed({
                                                        val itemCount = binding.introPager.adapter?.itemCount ?: 0
                                                        val currentPage = binding.introPager.currentItem
                                                        if (currentPage < itemCount - 1) binding.introPager.setCurrentItem(
                                                            currentPage + 1,
                                                            true
                                                        )
                                                    }, SLIDER_INTERVAL_TIME)
    }

    /**
     * Binds the [IntroPagerAdapter] to the ViewPager2 and sets up page change
     * handling.
     *
     * This method initializes the ViewPager2's adapter and registers a callback
     * to handle page change events. The callback updates the selected tab
     * margins, modifies the "Skip" button text (to "Done" on the last page), and
     * restarts the auto-slider.
     */
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

    /**
     * Generates and attaches the pager indicator (TabLayout) to the ViewPager2.
     *
     * This method uses [TabLayoutMediator] to synchronize the TabLayout with the
     * ViewPager2, creating the visual indicator for the current page.
     */
    private fun generatePagerIndicator() {
        val pagerIndicator = binding.pagerIndicator
        TabLayoutMediator(pagerIndicator, binding.introPager) { _, _ -> }.attach()
    }

    /**
     * Updates the margins of the selected tab in the pager indicator.
     *
     * This method adjusts the margins of the currently selected tab in the
     * TabLayout to visually highlight it.
     *
     * @param currentTab The index of the currently selected tab.
     */
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
        /**
         * The interval time (in milliseconds) for automatic page sliding.
         */
        const val SLIDER_INTERVAL_TIME = 3000L
    }
}
