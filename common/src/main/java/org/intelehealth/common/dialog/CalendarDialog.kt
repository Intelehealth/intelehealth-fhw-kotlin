package org.intelehealth.common.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.resource.R as ResourceR
import java.util.Calendar
import java.util.TimeZone

/**
 * Created by Vaghela Mithun R. on 11-07-2024 - 15:59.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * A custom dialog for selecting a date.
 *
 * This dialog extends [AppCompatDialogFragment] and provides a user interface
 * for selecting a date using a [DatePicker]. It allows setting minimum and
 * maximum selectable dates, a pre-selected date, and a date format.  It also
 * includes a listener interface for receiving the selected date.
 *
 * To use this dialog, create an instance using the [Builder] and show it using a
 * [FragmentManager].
 */
class CalendarDialog private constructor() : AppCompatDialogFragment(), OnDateSetListener {
    private var maxDate: Long = 0
    private var minDate: Long = 0
    private var selectedDate: Long = Calendar.getInstance().timeInMillis
    private var format: String = "MMM dd, yyyy"
    private lateinit var listener: OnDatePickListener

    /**
     * Listener interface for receiving the selected date from the dialog.
     */
    interface OnDatePickListener {
        /**
         * Called when a date is selected in the dialog.
         *
         * @param day The day of the month (1-31).
         * @param month The month (0-11, where 0 is January).
         * @param year The year.
         * @param value The formatted date string, according to the specified
         *   format.
         */
        fun onDatePick(day: Int, month: Int, year: Int, value: String?)
    }

    /**
     * Creates the dialog instance.
     *
     * This function is called by the system to create the dialog. It initializes
     * a [DatePickerDialog], sets the initial date, and configures minimum and
     * maximum dates if provided.  It also sets a listener to update the button
     * theme when the dialog is shown.
     *
     * @param savedInstanceState If the fragment is being re-created from a
     *   previous saved state, this is the state.
     * @return A new [DatePickerDialog] instance.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this, year, month, day
        ).apply {
            if (maxDate > 0) datePicker.maxDate = maxDate
            if (minDate > 0) datePicker.minDate = minDate
        }

        datePickerDialog.setOnShowListener {
            updateButtonTheme(datePickerDialog)
        }

        return datePickerDialog
    }

    /**
     * Updates the theme of the dialog buttons.
     *
     * This function sets the text color of the positive and negative buttons in
     * the [DatePickerDialog] to a specified color (likely your app's accent
     * color).
     *
     * @param datePickerDialog The [DatePickerDialog] instance.
     */
    private fun updateButtonTheme(datePickerDialog: DatePickerDialog) {
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    ResourceR.color.colorAccent
                )
            ) // Change to your desired color

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), ResourceR.color.colorAccent))
    }

    /**
     * Callback for when a date is set in the [DatePicker].
     *
     * This function is called when the user selects a date in the dialog. It
     * retrieves the selected date, formats it according to the specified
     * format, and calls the [OnDatePickListener.onDatePick] method with the
     * selected date information.
     *
     * @param datePicker The [DatePicker] view.
     * @param year The selected year.
     * @param month The selected month (0-11).
     * @param day The selected day of the month (1-31).
     */
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        val sdf = DateTimeUtils.getSimpleDateFormat(format, TimeZone.getDefault())
        val formattedDate = sdf.format(calendar.time)

        if (::listener.isInitialized) listener.onDatePick(day, month, year, formattedDate)
    }

    /**
     * Builder class for creating instances of [CalendarDialog].
     *
     * This class provides a fluent API for configuring the [CalendarDialog]
     * before creating it.  It allows setting the maximum and minimum selectable
     * dates, the pre-selected date, the date format, and the listener for date
     * selection events.
     */
    class Builder {
        private val calendarDialog = CalendarDialog()

        /**
         * Sets the maximum selectable date.
         *
         * @param max The maximum date in milliseconds since the epoch.
         * @return The [Builder] instance for chaining.
         */
        fun maxDate(max: Long): Builder {
            calendarDialog.maxDate = max
            return this
        }

        /**
         * Sets the minimum selectable date.
         *
         * @param min The minimum date in milliseconds since the epoch.
         * @return The [Builder] instance for chaining.
         */
        fun minDate(min: Long): Builder {
            calendarDialog.minDate = min
            return this
        }

        /**
         * Sets the pre-selected date.
         *
         * @param selected The pre-selected date in milliseconds since the epoch.
         * @return The [Builder] instance for chaining.
         */
        fun selectedDate(selected: Long): Builder {
            calendarDialog.selectedDate = selected
            return this
        }

        /**
         * Sets the date format string.
         *
         * The format string should follow the patterns defined in
         * [java.text.SimpleDateFormat].
         *
         * @param format The date format string.
         * @return The [Builder] instance for chaining.
         */
        fun format(format: String): Builder {
            calendarDialog.format = format
            return this
        }

        /**
         * Sets the listener for date selection events.
         *
         * @param listener The [OnDatePickListener] to receive date selection
         *   events.
         * @return The [Builder] instance for chaining.
         */
        fun listener(listener: OnDatePickListener): Builder {
            calendarDialog.listener = listener
            return this
        }

        /**
         * Creates a [CalendarDialog] instance with the configured settings.
         *
         * @return A new [CalendarDialog] instance.
         */
        fun build() = calendarDialog
    }

    companion object {
        const val TAG = "CalendarDialog"

        /**
         * Shows a pre-configured [CalendarDialog] for a common use case.
         *
         * This function provides a convenient way to show a date picker dialog
         * with the following default settings:
         * - Maximum date: Current date.
         * - Selected date: Current date.
         * - Format: "MMM dd, yyyy" (e.g., "Jul 21, 2024").  Uses the constant
         *   `DateTimeUtils.MMM_DD_YYYY_FORMAT`, assuming it's defined in your
         *   project.
         *
         * @param listener The [OnDatePickListener] to receive the selected date.
         * @param childFragmentManager The [FragmentManager] to use for showing
         *   the dialog (typically the child fragment manager of the calling
         *   fragment).
         */
        fun showDatePickerDialog(listener: OnDatePickListener, childFragmentManager: FragmentManager) {
            Builder()
                .maxDate(Calendar.getInstance().timeInMillis)
                .selectedDate(Calendar.getInstance().timeInMillis)
                .format(DateTimeUtils.MMM_DD_YYYY_FORMAT)
                .listener(listener)
                .build().show(childFragmentManager, TAG)
        }
    }
}
