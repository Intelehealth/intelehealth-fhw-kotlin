package org.intelehealth.app.ui.achievement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAchievementsBinding
import org.intelehealth.app.ui.achievement.adapter.AchievementPagerAdapter

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 17:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class MyAchievementFragment : Fragment(R.layout.fragment_achievements) {
    private lateinit var binding: FragmentAchievementsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAchievementsBinding.bind(view)
        binding.level = 1
        binding.points = 0
        bindPagerAdapter()
    }

    private fun bindPagerAdapter() {
        val adapter = AchievementPagerAdapter(childFragmentManager, lifecycle, requireContext())
        binding.pagerAchievements.adapter = adapter
        TabLayoutMediator(binding.tabAchievements, binding.pagerAchievements) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }
}
