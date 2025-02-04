package org.intelehealth.app.ui.binding

import android.view.View
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import org.intelehealth.config.room.entity.PatientRegistrationFields

/**
 * Created by Vaghela Mithun R. on 11-07-2024 - 19:55.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A [BindingAdapter] function that sets the checked button in a
 * [MaterialButtonToggleGroup] based on a gender string.
 *
 * This function is designed to be used in data binding layouts to simplify the
 * process of setting the selected gender in a toggle group.
 *
 * @param btnToggleGroup The [MaterialButtonToggleGroup] to set the checked button in.
 * @param gender The gender string (e.g., "M", "F", "Other").
 *               If the gender is "M" (case-insensitive), the button with ID
 *               `R.id.btnMale` is checked. If it's "F" (case-insensitive),
 *               `R.id.btnFemale` is checked. Otherwise, `R.id.btnOther` is checked.
 */
@BindingAdapter("gender")
fun genderViewBinding(btnToggleGroup: MaterialButtonToggleGroup?, gender: String?) {
    if (btnToggleGroup != null && gender != null) {
//        if (gender.equals("M", ignoreCase = true)) btnToggleGroup.check(R.id.btnMale)
//        else if (gender.equals("F", ignoreCase = true)) btnToggleGroup.check(R.id.btnFemale)
//        else btnToggleGroup.check(R.id.btnOther)
    }
}

/**
 * A [BindingAdapter] function that sets the minimum value of a [NumberPicker].
 *
 * This function is designed to be used in data binding layouts to simplify
 * setting the minimum value of a [NumberPicker].
 *
 * @param numberPicker The [NumberPicker] to set the minimum value for.
 * @param value The minimum value to set. If `null`, no action is taken.
 */
@BindingAdapter("minNumber")
fun bindMinValue(numberPicker: NumberPicker?, value: Int?) {
    if (numberPicker != null && value != null) {
        numberPicker.minValue = value
    }
}

/**
 * A [BindingAdapter] function that sets the maximum value of a [NumberPicker].
 *
 * This function is designed to be used in data binding layouts to simplify
 * setting the maximum value of a [NumberPicker].
 *
 * @param numberPicker The [NumberPicker] to set the maximum value for.
 * @param value The maximum value to set. If `null`, no action is taken.
 */
@BindingAdapter("maxNumber")
fun bindMaxValue(numberPicker: NumberPicker?, value: Int?) {
    if (numberPicker != null && value != null) {
        numberPicker.maxValue = value
    }
}

/**
 * A [BindingAdapter] function that enables or disables a [View] based on
 * configuration and edit mode.
 *
 * This function is designed to be used in data binding layouts to control
 * whether a view is editable or not, based on the `isEditable` property of a
 * [PatientRegistrationFields] object and a boolean `editMode` flag.
 *
 * The view is enabled (editable) if:
 * - `config.isEditable` is `false` AND `editMode` is `true`.
 * Otherwise, the view is disabled (not editable).
 *
 * @param view The [View] to enable or disable.
 * @param config The [PatientRegistrationFields] object containing the
 *               `isEditable` property.
 * @param editMode A boolean flag indicating whether the edit mode is active.
 */
@BindingAdapter(value = ["config", "editMode"], requireAll = true)
fun changeEditMode(view: View?, config: PatientRegistrationFields?, editMode: Boolean) {
    if (view != null && config != null) {
        view.isEnabled = (!config.isEditable && editMode).not()
    }
}

/**
 * A [BindingAdapter] function that dynamically adjusts the start margin of a
 * [View] based on configuration and a specified margin value.
 *
 * This function is designed to be used in data binding layouts to control the
 * start margin of a view dynamically. The margin is applied only if the
 * `isEnabled` property of a [PatientRegistrationFields] object is `true`.
 *
 * @param view The [View] to adjust the start margin for.
 * @param config The [PatientRegistrationFields] object containing the
 *               `isEnabled` property.
 * @param margin The margin value (in density-independent pixels) to apply when
 *               `config.isEnabled` is `true`. If `config.isEnabled` is `false`,
 *               the margin is set to 0.
 */
@BindingAdapter(value = ["config", "dynamicMargin"], requireAll = true)
fun maintainDynamicMargin(view: View?, config: PatientRegistrationFields?, margin: Float) {
    if (view != null && config != null) {
        val param = view.layoutParams as ConstraintLayout.LayoutParams
        param.marginStart = if (config.isEnabled) margin.toInt() else 0
        view.layoutParams = param
    }
}

