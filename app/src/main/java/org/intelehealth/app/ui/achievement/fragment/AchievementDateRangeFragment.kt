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

    private fun observeData() {
        viewModel.dateRangeAchievementLiveData.observe(viewLifecycleOwner) { achievement ->
            binding.apply {
                binding.achievementScore = achievement
            }
        }
    }

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

    private fun fetchRecordData() {
        val fromTimeInMillis = binding.btnFromDate.tag as Long
        val toTimeInMillis = binding.btnToDate.tag as Long
        val fromDate = DateTimeUtils.formatToLocalDate(Date(fromTimeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN)
        val toDate = DateTimeUtils.formatToLocalDate(Date(toTimeInMillis), DateTimeUtils.YYYY_MM_DD_HYPHEN)
        viewModel.fetchDateRangeAchievement(fromDate, toDate)
    }

    private fun showFromDatePickerDialog(selectedDate: Long) {
        CalendarDialog.Builder()
            .maxDate(Calendar.getInstance().timeInMillis)
            .selectedDate(selectedDate)
            .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
            .listener(fromDateListener)
            .build().show(childFragmentManager, CalendarDialog.TAG)
    }

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

    private fun generateToDateWithAddOnDaysInFromDate(dayOfMonth: Int): Long {
        return Calendar.getInstance().apply {
            timeInMillis = binding.btnFromDate.tag as Long
        }.also { it.add(Calendar.DAY_OF_MONTH, dayOfMonth) }.timeInMillis
    }

    private val fromDateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnFromDate.text = it }
            setFromSelectedDate(day, month, year)
        }
    }

    private val toDateListener = object : CalendarDialog.OnDatePickListener {
        override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
            value?.let { binding.btnToDate.text = it }
            setToSelectedDate(day, month, year)
        }
    }

    private fun setFromSelectedDate(day: Int, month: Int, year: Int) {
        getUpdatedCalendar(day, month, year).also {
            binding.btnFromDate.tag = it.timeInMillis
            val toDateMilli = binding.btnToDate.tag as Long
            if (toDateMilli < it.timeInMillis) updateToDate(day, month, year)
            else fetchRecordData()
        }
    }

    private fun updateToDate(day: Int, month: Int, year: Int) {
        val currentTime = Calendar.getInstance().timeInMillis
        val toDate = getUpdatedCalendar(day, month, year).apply {
            add(Calendar.DAY_OF_MONTH, 7)
        }.timeInMillis
        if (toDate <= currentTime) updateToDate(toDate)
        else updateToDate(currentTime)
    }

    private fun updateToDate(timeInMilli: Long) {
        getUpdatedCalendar(timeInMilli).also {
            setToSelectedDate(
                it.get(Calendar.DAY_OF_MONTH),
                it.get(Calendar.MONTH),
                it.get(Calendar.YEAR)
            )
        }
    }

    private fun setToSelectedDate(day: Int, month: Int, year: Int) {
        getUpdatedCalendar(day, month, year).also {
            binding.btnToDate.tag = it.timeInMillis
            fetchRecordData()
        }
    }

    private fun getUpdatedCalendar(day: Int, month: Int, year: Int) = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, day)
    }

    private fun getUpdatedCalendar(timeInMilli: Long) = Calendar.getInstance().apply { timeInMillis = timeInMilli }
}
