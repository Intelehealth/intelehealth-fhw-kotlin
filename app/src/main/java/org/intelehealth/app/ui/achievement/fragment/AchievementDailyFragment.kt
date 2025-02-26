package org.intelehealth.app.ui.achievement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentDailyAchievementsBinding
import org.intelehealth.app.databinding.FragmentOverallAchievementsBinding

/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 18:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class AchievementDailyFragment : Fragment(R.layout.fragment_daily_achievements) {
    private lateinit var binding: FragmentDailyAchievementsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyAchievementsBinding.bind(view)
    }
}
