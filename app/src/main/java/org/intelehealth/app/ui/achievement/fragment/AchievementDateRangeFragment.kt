package org.intelehealth.app.ui.achievement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentDateRangeAchievementsBinding
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
 * Fragment to display the user's achievements within a selected date range.
 *
 * This fragment allows users to specify a start and end date and view their
 * achievements for that period. It includes date pickers for both the "from"
 * and "to" dates and updates the displayed data accordingly.
 */
@AndroidEntryPoint
class AchievementDateRangeFragment : Fragment(R.layout.fragment_date_range_achievements) {
    private lateinit var binding: FragmentDateRangeAchievementsBinding
    private val viewModel: AchievementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDateRangeAchievementsBinding.bind(view)
        observeData()
        setupFromDateRange()
        setupToDateRange()
        fetchRecordData()
    }

    /**
     * Observes the date range achievement data from the ViewModel and updates the UI.
     */
    private fun observeData() {
        viewModel.dateRangeAchievementLiveData.observe(viewLifecycleOwner) { achievement ->
            binding.apply {
                binding.achievementScore = achievement
            }
        }
    }

    /**
     * Sets up the initial date and click listener for the "from" date selection button.
     * The initial "from" date is set to 7 days ago.
     */
    private fun setupFromDateRange() {
        binding.btnFromDate.tag = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -7)
        }.timeInMillis

        DateTimeUtils.formatToLocalDate(
            Date(binding.btnFromDate.tag as Long),
            DateTimeUtils.MMM_DD_YYYY_FORMAT
        ).apply { binding.btnFromDate.text = this }

        binding.btnFromDate.setOnClickListener {
            showFromDatePickerDialog(it?.tag as Long)
        }
    }

    /**
     * Sets up the initial date and click listener for the "to" date selection button.
     * The initial "to" date is set to the current date.
     */
    private fun setupToDateRange() {
        binding.btnToDate.tag = Calendar.getInstance().timeInMillis

        DateTimeUtils.formatToLocalDate(
            Date(binding.btnToDate.tag as Long),
            DateTimeUtils.MMM_DD_YYYY_FORMAT
        ).apply { binding.btnToDate.text = this }

        binding.btnToDate.setOnClickListener {
            showToDatePickerDialog(it?.tag as Long)
        }
    }

    /**
     * Fetches the achievement data for the selected date range.
     */
    private fun fetchRecordData() {
        val fromTimeInMillis = binding.btnFromDate.tag as Long
        val toTimeInMillis = binding.btnToDate.tag as Long
        val fromDate = DateTimeUtils.formatToLocalDate(Date(fromTimeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN)
        val toDate = DateTimeUtils.formatToLocalDate(Date(toTimeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN)
        viewModel.fetchDateRangeAchievement(fromDate, toDate)
    }

    /**
     * Shows a date picker dialog for selecting the "from" date.
     *
     * @param selectedDate The currently selected "from" date in milliseconds.
     */
    private fun showFromDatePickerDialog(selectedDate: Long) {
        CalendarDialog.Builder()
            .maxDate(Calendar.getInstance().timeInMillis)
            .selectedDate(selectedDate)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
            .listener(fromDateListener)
            .build().show(childFragmentManager, CalendarDialog.TAG)
    }

    /**
     * Shows a date picker dialog for selecting the "to" date.
     * Ensures that the "to" date is not before the "from" date.
     *
     * @param selectedDate The currently selected "to" date in milliseconds.
     */
    private fun showToDatePickerDialog(selectedDate: Long) {
        val maxDate = Calendar.getInstance().timeInMillis
        val minDate = generateToDateWithAddOnDaysInFromDate(1)

        val selected = if (selectedDate in minDate..maxDate) selectedDate else minDate

        CalendarDialog.Builder()
            .maxDate(maxDate)
            .minDate(minDate)
            .selectedDate(selected)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
            .listener(toDateListener)
            .build().show(childFragmentManager, CalendarDialog.TAG)
    }

    /**
     * Generates a date that is one day after the currently selected "from" date.
     * This is used as the minimum selectable date for the "to" date picker.
     *
     * @param dayOfMonth The number of days to add to the "from" date (should be 1).
     * @return The time in milliseconds representing the date one day after the "from" date.
     */
    private fun generateToDateWithAddOnDaysInFromDate(dayOfMonth: Int): Long {
        return Calendar.getInstance().apply {
            timeInMillis = binding.btnFromDate.tag as Long
        }.also { it.add(Calendar.DAY_OF_MONTH, dayOfMonth) }.timeInMillis
    }

    /**
     * Listener for the "from" date picker dialog.
     */
    private val fromDateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnFromDate.text = it }
            setFromSelectedDate(day, month, year)
        }
    }

    /**
     * Listener for the "to" date picker dialog.
     */
    private val toDateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnToDate.text = it }
            setToSelectedDate(day, month, year)
        }
    }

    /**
     * Sets the selected "from" date and updates the UI and data.
     * If the new "from" date is after the current "to" date, it also updates the "to" date.
     *
     * @param day The selected day of the month.
     * @param month The selected month (0-indexed).
     * @param year The selected year.
     */
    private fun setFromSelectedDate(day: Int, month: Int, year: Int) {
        getUpdatedCalendar(day, month, year).also {
            binding.btnFromDate.tag = it.timeInMillis
            val toDateMilli = binding.btnToDate.tag as Long
            if (toDateMilli < it.timeInMillis) updateToDate(day, month, year)
            else fetchRecordData()
        }
    }

    /**
     * Updates the "to" date to maintain a valid date range.
     * If the new "from" date is after the current "to" date, this method adjusts the "to" date.
     *
     * @param day The day of the month of the new "from" date.
     * @param month The month (0-indexed) of the new "from" date.
     * @param year The year of the new "from" date.
     */
    private fun updateToDate(day: Int, month: Int, year: Int) {
        val currentTime = Calendar.getInstance().timeInMillis
        val toDate = getUpdatedCalendar(day, month, year).apply {
            add(Calendar.DAY_OF_MONTH, 7)
        }.timeInMillis
        if (toDate <= currentTime) updateToDate(toDate)
        else updateToDate(currentTime)
    }

    /**
     * Updates the "to" date to a specific time in milliseconds.
     *
     * @param timeInMilli The new "to" date in milliseconds.
     */
    private fun updateToDate(timeInMilli: Long) {
        getUpdatedCalendar(timeInMilli).also {
            setToSelectedDate(
                it.get(Calendar.DAY_OF_MONTH),
                it.get(Calendar.MONTH),
                it.get(Calendar.YEAR)
            )
        }
    }

    /**
     * Sets the selected "to" date and updates the UI and data.
     *
     * @param day The selected day of the month.
     * @param month The selected month (0-indexed).
     * @param year The selected year.
     */
    private fun setToSelectedDate(day: Int, month: Int, year: Int) {
        getUpdatedCalendar(day, month, year).also {
            binding.btnToDate.tag = it.timeInMillis
            fetchRecordData()
        }
    }

    /**
     * Creates a Calendar instance with the specified date.
     *
     * @param day The day of the month.
     * @param month The month (0-indexed).
     * @param year The year.
     * @return A Calendar instance set to the given date.
     */
    private fun getUpdatedCalendar(day: Int, month: Int, year: Int) = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, day)
    }

    /**
     * Creates a Calendar instance with the specified time in milliseconds.
     *
     * @param timeInMilli The time in milliseconds.
     * @return A Calendar instance set to the given time.
     */
    private fun getUpdatedCalendar(timeInMilli: Long) = Calendar.getInstance().apply { timeInMillis = timeInMilli }
}
