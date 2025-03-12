package org.intelehealth.app.ui.achievement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentDailyAchievementsBinding
import org.intelehealth.app.ui.achievement.viewmodel.AchievementViewModel
import org.intelehealth.common.dialog.CalendarDialog
import org.intelehealth.common.utility.DateTimeUtils
import java.util.Calendar
import java.util.Date

/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 18:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
class AchievementDailyFragment : Fragment(R.layout.fragment_daily_achievements) {
    private lateinit var binding: FragmentDailyAchievementsBinding
    private val viewModel: AchievementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyAchievementsBinding.bind(view)
        observeData()
        setupRecordDate()
    }

    private fun setupRecordDate() {
        binding.btnCurrentDate.tag = Calendar.getInstance().timeInMillis

        DateTimeUtils.formatToLocalDate(
            Calendar.getInstance().time,
            DateTimeUtils.MMM_DD_YYYY_FORMAT
        ).apply { binding.btnCurrentDate.text = this }

        binding.btnCurrentDate.setOnClickListener {
            showDatePickerDialog(it?.tag as Long)
        }

        fetchRecordData()
    }

    private fun fetchRecordData() {
        val timeInMillis = binding.btnCurrentDate.tag as Long
        DateTimeUtils.formatToLocalDate(Date(timeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN).apply {
            viewModel.fetchDailyAchievement(this)
        }
    }

    private fun observeData() {
        viewModel.dailyAchievementLiveData.observe(viewLifecycleOwner) { achievement ->
            binding.apply {
                binding.achievementScore = achievement
            }
        }
    }

    private fun showDatePickerDialog(selectedDate: Long) {
        CalendarDialog.Builder()
            .maxDate(Calendar.getInstance().timeInMillis)
            .selectedDate(selectedDate)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
            .listener(dateListener)
            .build().show(childFragmentManager, CalendarDialog.TAG)
    }

    private val dateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnCurrentDate.text = it }
            setSelectedDate(day, month, year)
        }
    }

    private fun setSelectedDate(day: Int, month: Int, year: Int) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }.also {
            binding.btnCurrentDate.tag = it.timeInMillis
            fetchRecordData()
        }
    }
}
