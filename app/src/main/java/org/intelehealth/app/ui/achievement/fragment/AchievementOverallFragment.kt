package org.intelehealth.app.ui.achievement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOverallAchievementsBinding
import org.intelehealth.app.ui.achievement.viewmodel.AchievementViewModel

/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 18:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * Fragment to display the user's overall achievements.
 *
 * This fragment provides an overview of the user's achievements, including
 * their total score and other relevant metrics. It fetches the data from
 * the ViewModel and updates the UI accordingly.
 */
@AndroidEntryPoint
class AchievementOverallFragment : Fragment(R.layout.fragment_overall_achievements) {
    private lateinit var binding: FragmentOverallAchievementsBinding
    private val viewModel: AchievementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOverallAchievementsBinding.bind(view)
        observeData()
    }

    /**
     * Observes the overall achievement data from the ViewModel and updates the UI.
     */
    private fun observeData() {
        viewModel.fetchOverallAchievement()
        viewModel.overallAchievementLiveData.observe(viewLifecycleOwner) { achievement ->
            Timber.d { "Achievement => $achievement" }
            binding.apply {
                binding.achievementScore = achievement
            }
        }
    }
}
