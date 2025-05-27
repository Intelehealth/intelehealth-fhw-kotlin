package org.intelehealth.common.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import org.intelehealth.common.R
import org.intelehealth.common.databinding.DialogAgePickerBinding

/**
 * Created by Vaghela Mithun R. on 08-05-2025 - 18:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AgePickerDialog private constructor() : AppCompatDialogFragment(R.layout.dialog_age_picker) {
    private lateinit var binding: DialogAgePickerBinding
    private var agePickListener: OnAgePickListener? = null

    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0
    private var maxYear: Int = MAX_YEARS
    private var minYear: Int = MIN_YEARS
    private var maxMonth: Int = MAX_MONTHS
    private var minMonth: Int = MIN_MONTHS
    private var maxDay: Int = MAX_DAYS
    private var minDay: Int = MIN_DAYS

    interface OnAgePickListener {
        fun onAgePick(year: Int, month: Int, day: Int)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogAgePickerBinding.bind(view)
        setUpPicker()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun setUpPicker() {
        binding.npYear.minValue = minYear
        binding.npYear.maxValue = maxYear
        binding.npYear.value = selectedYear

        binding.npMonth.minValue = minMonth
        binding.npMonth.maxValue = maxMonth
        binding.npMonth.value = selectedMonth

        binding.npDays.minValue = minDay
        binding.npDays.maxValue = maxDay
        binding.npDays.value = selectedDay

        setUpPickerClickListener()
    }

    private fun setUpPickerClickListener() {
        binding.btnOkay.setOnClickListener {
            agePickListener?.onAgePick(
                binding.npYear.value,
                binding.npMonth.value,
                binding.npDays.value
            )
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    class Builder {
        private val agePickerDialog = AgePickerDialog()

        fun maxYear(year: Int = MAX_YEARS): Builder {
            agePickerDialog.maxYear = year
            return this
        }

        fun minYear(year: Int = MIN_YEARS): Builder {
            agePickerDialog.minYear = year
            return this
        }

        fun selectedYear(year: Int): Builder {
            agePickerDialog.selectedYear = year
            return this
        }

        fun maxMonth(month: Int = MAX_MONTHS): Builder {
            agePickerDialog.maxMonth = month
            return this
        }

        fun minMonth(month: Int = MIN_MONTHS): Builder {
            agePickerDialog.minMonth = month
            return this
        }

        fun selectedMonth(month: Int): Builder {
            agePickerDialog.selectedMonth = month
            return this
        }

        fun maxDay(day: Int = MAX_DAYS): Builder {
            agePickerDialog.maxDay = day
            return this
        }

        fun minDay(day: Int = MIN_DAYS): Builder {
            agePickerDialog.minDay = day
            return this
        }

        fun selectedDay(day: Int): Builder {
            agePickerDialog.selectedDay = day
            return this
        }

        fun agePickListener(listener: OnAgePickListener): Builder {
            agePickerDialog.agePickListener = listener
            return this
        }

        fun build() = agePickerDialog
    }

    companion object {
        const val TAG = "AgePickerDialog"
        const val MAX_YEARS = 100
        const val MIN_YEARS = 0
        const val MAX_MONTHS = 11
        const val MIN_MONTHS = 0
        const val MAX_DAYS = 30
        const val MIN_DAYS = 0
    }
}
