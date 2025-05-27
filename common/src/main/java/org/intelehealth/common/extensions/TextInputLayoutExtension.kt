package org.intelehealth.common.extensions

import android.text.InputFilter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 12-07-2024 - 11:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun TextInputLayout.showError(@StringRes resId: Int) {
//    Timber.d { "showError" }
    error = context.getString(resId)
}

fun TextInputLayout.showError(message: String) {
//    Timber.d { "showError" }
    error = message
}

fun TextInputLayout.hideError() {
//    Timber.d { "hideError" }
    isErrorEnabled = false
}

fun TextInputLayout.showError() {
//    Timber.d { "showError" }
    isErrorEnabled = false
}

fun TextInputLayout.hideErrorOnTextChang(input: TextInputEditText) {
    input.doOnTextChanged { text, _, _, _ ->
//        Timber.d { "hideErrorOnTextChang" }
        if (text?.length!! > 0) hideError()
    }
}

fun TextInputLayout.hideDigitErrorOnTextChang(input: TextInputEditText, digit: Int) {
    input.doOnTextChanged { text, _, _, _ ->
//        Timber.d { "phone validation $count == $digit" }
        if (text?.length == digit) hideError() else showError()
    }
}

fun TextInputLayout.validate(input: TextInputEditText, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else true
}

fun TextInputLayout.validate(input: TextInputEditText, errorMessage: String): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(errorMessage)
        false
    } else true
}

fun TextInputLayout.validateDropDown(input: AutoCompleteTextView, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else true
}

fun TextInputLayout.showDropDownError(input: String?, errorMessage: String): Boolean {
    return if (input.isNullOrEmpty()) {
        showError(errorMessage)
        false
    } else true

}

fun TextInputLayout.validateDigit(
    input: TextInputEditText,
    @StringRes resId: Int,
    minDigit: Int,
): Boolean {
    return validateDigit(input, context.getString(resId), minDigit)
}

fun TextInputLayout.validateDigit(
    input: TextInputEditText,
    error: String,
    minDigit: Int,
): Boolean {
    return if (input.text.isNullOrEmpty() || input.text?.length!! < minDigit || input.text?.all { it.isDigit().not() } == true) {
        showError(error)
        false
    } else true
}

fun TextInputLayout.validatePassword(input: TextInputEditText, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else if (input.text?.length!! < MIN_PASSWORD_LENGTH) {
        showError(ResourceR.string.error_invalid_password)
        false
    } else true
}

fun TextInputLayout.validatePasswordPattern(input: TextInputEditText, @StringRes resId: Int): Boolean {
    return if (!isValidPassword(input.text.toString())) {
        showError(resId)
        false
    } else true
}

fun TextInputLayout.passwordMatchWithConfirmPassword(
    newPassword: TextInputEditText, confirmPassword: TextInputEditText
): Boolean {
    return if (!newPassword.text.toString().equals(confirmPassword.text.toString(), ignoreCase = false)) {
        showError(ResourceR.string.error_confirm_password)
        false
    } else true
}

fun isValidPassword(passwd: String?): Boolean {
    if (passwd.isNullOrEmpty()) return false
    //String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}"; // with special character
    val pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}" // without special character
    return passwd.matches(pattern.toRegex())
}

fun EditText.addFilter(filter: InputFilter) {
    this.filters += filter
}

fun TextInputLayout.changeButtonStateOnTextChange(input: TextInputEditText, button: Button) {
    input.doOnTextChanged { text, _, _, _ ->
        if (text?.length!! > 0) {
            button.isEnabled = true
            hideError()
        } else button.isEnabled = false
    }
}

fun TextInputLayout.validateEmail(input: TextInputEditText, @StringRes resId: Int): Boolean {
    return if (!isValidEmail(input.text.toString())) {
        showError(resId)
        false
    } else true
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}

fun TextInputLayout.validateDropDowb(input: AutoCompleteTextView, @StringRes resId: Int): Boolean {
    return if (input.text.isNullOrEmpty()) {
        showError(resId)
        false
    } else true
}
