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

/**
 * Fragment to display the user's daily achievements.
 *
 * This fragment allows users to view their achievements for a specific day.
 * It includes a date picker to select the day and displays the corresponding
 * achievement data.
 */
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

    /**
     * Sets up the initial date and click listener for the date selection button.
     */
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

    /**
     * Fetches the achievement data for the currently selected date.
     */
    private fun fetchRecordData() {
        val timeInMillis = binding.btnCurrentDate.tag as Long
        DateTimeUtils.formatToLocalDate(Date(timeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN).apply {
            viewModel.fetchDailyAchievement(this)
        }
    }

    /**
     * Observes the daily achievement data from the ViewModel and updates the UI.
     */
    private fun observeData() {
        viewModel.dailyAchievementLiveData.observe(viewLifecycleOwner) { achievement ->
            binding.apply {
                binding.achievementScore = achievement
            }
        }
    }

    /**
     * Shows a date picker dialog to allow the user to select a different date.
     *
     * @param selectedDate The currently selected date in milliseconds.
     */
    private fun showDatePickerDialog(selectedDate: Long) {
        CalendarDialog.Builder()
            .maxDate(Calendar.getInstance().timeInMillis)
            .selectedDate(selectedDate)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
            .listener(dateListener)
            .build().show(childFragmentManager, CalendarDialog.TAG)
    }

    /**
     * Listener for the date picker dialog.
     */
    private val dateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnCurrentDate.text = it }
            setSelectedDate(day, month, year)
        }
    }

    /**
     * Sets the selected date based on the date picker result and fetches new data.
     *
     * @param day The selected day of the month.
     * @param month The selected month (0-indexed).
     * @param year The selected year.
     */
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
