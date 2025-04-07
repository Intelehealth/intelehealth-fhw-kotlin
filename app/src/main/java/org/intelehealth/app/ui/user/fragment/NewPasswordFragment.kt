package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentResetPasswordBinding
import org.intelehealth.app.databinding.ViewNewPasswordBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.passwordMatchWithConfirmPassword
import org.intelehealth.common.extensions.setCompoundDrawableClick
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.extensions.showTooltip
import org.intelehealth.common.extensions.startWhatsappIntent
import org.intelehealth.common.extensions.validatePasswordPattern
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Fragment for resetting the user's password.
 *
 * This fragment allows the user to reset their password by entering a new
 * password and confirming it. It also provides a button to generate a random
 * password and a button to contact support via WhatsApp.
 *
 * The fragment interacts with [UserViewModel] to perform the password reset
 * operation and to generate a random password. It also uses [BaseProgressFragment]
 * to handle progress view and error handling.
 *
 * The fragment uses [navArgs] to receive the user's UUID as an argument.
 */
@AndroidEntryPoint
abstract class NewPasswordFragment(@LayoutRes layoutResId: Int) : BaseProgressFragment(layoutResId) {
    private lateinit var bindingNewPassword: ViewNewPasswordBinding
    override val viewModel by viewModels<UserViewModel>()
    val args by navArgs<ResetPasswordFragmentArgs>()

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been restored in to the view.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingNewPassword = attachNewPasswordView()
        handleButtonClick()
        changeSaveButtonStateOnTextChange()
    }

    /**
     * Generates a random password and sets it in the new password and confirm password fields.
     *
     * This function calls [UserViewModel.generatePassword] to generate a random password
     * and then sets it in the `textInputNewPassword` and `textInputConfirmPassword`
     * EditTexts. It also updates the save button state.
     */
    private fun generatePassword() {
        viewModel.generatePassword().apply {
            bindingNewPassword.textInputNewPassword.setText(this)
            bindingNewPassword.textInputConfirmPassword.setText(this)
            changeSaveButtonState()
        }
    }

    /**
     * Handles the click events for the buttons in the fragment.
     *
     * This function sets click listeners for the following buttons:
     * - `btnHelp`: Starts a WhatsApp intent to contact support.
     * - `btnResetPasswordSave`: Validates the fields and requests a password reset.
     * - `tvGenerateNewPassword`: Generates a new password.
     * - `tvGenerateNewPassword` compound drawable: Shows a tooltip.
     * - `toolbar` navigation icon: Pops the back stack.
     */
    private fun handleButtonClick() {
        bindingNewPassword.btnResetPasswordSave.setOnClickListener {
            validateFields { requestResetPassword() }
        }

        bindingNewPassword.tvGenerateNewPassword.setOnClickListener { generatePassword() }
        bindingNewPassword.tvGenerateNewPassword.setCompoundDrawableClick(Gravity.END) {
            it.showTooltip(ResourceR.string.content_generate_password_tooltip_text)
        }
    }

    /**
     * Changes the save button state based on text changes in the password fields.
     *
     * This function sets text change listeners for the `textInputNewPassword` and
     * `textInputConfirmPassword` EditTexts. When the text changes, it updates the
     * save button state and hides any error messages.
     */
    private fun changeSaveButtonStateOnTextChange() {
        bindingNewPassword.textInputNewPassword.doOnTextChanged { _, _, _, _ ->
            changeSaveButtonState()
            bindingNewPassword.textInputLayoutNewPassword.hideError()
        }
        bindingNewPassword.textInputConfirmPassword.doOnTextChanged { _, _, _, _ ->
            changeSaveButtonState()
            bindingNewPassword.textInputLayoutConfirmPassword.hideError()
        }
    }

    /**
     * Changes the save button state based on the validity of the password fields.
     *
     * This function checks if the new password and confirm password fields are
     * valid (not empty and at least [MIN_PASSWORD_LENGTH] characters long) and
     * enables or disables the `btnResetPasswordSave` button accordingly.
     */
    fun changeSaveButtonState() {
        val newPassword = bindingNewPassword.textInputNewPassword.text
        val confirmPassword = bindingNewPassword.textInputConfirmPassword.text
        val validNewPassword = newPassword?.isNotEmpty() == true && newPassword.length >= MIN_PASSWORD_LENGTH
        val validConfirmPassword =
            confirmPassword?.isNotEmpty() == true && confirmPassword.length >= MIN_PASSWORD_LENGTH
        bindingNewPassword.btnResetPasswordSave.isEnabled =
            validNewPassword.and(validConfirmPassword).and(validateCurrentPassword())
    }

    /**
     * Validates the password fields.
     *
     * This function validates the new password and confirm password fields using
     * [validatePasswordPattern] and [passwordMatchWithConfirmPassword]. If both
     * fields are valid, it invokes the provided callback.
     *
     * @param onValid A callback to be invoked if the fields are valid.
     */
    private fun validateFields(onValid: () -> Unit) {
        val validPassword = bindingNewPassword.textInputLayoutNewPassword.validatePasswordPattern(
            bindingNewPassword.textInputNewPassword, ResourceR.string.error_password_validation
        )

        val validConfirmPassword = bindingNewPassword.textInputLayoutConfirmPassword.passwordMatchWithConfirmPassword(
            bindingNewPassword.textInputNewPassword, bindingNewPassword.textInputConfirmPassword
        )

        if (validPassword.and(validConfirmPassword).and(validateCurrentPassword())) onValid()
    }

    /**
     * Requests a password reset.
     *
     * This function calls [UserViewModel.resetPassword] to request a password
     * reset with the new password. It observes the result and shows a success
     * message or an error message accordingly.
     */
    abstract fun requestResetPassword()

    /**
     * Returns the anchor view for the snackbar.
     *
     * @return The anchor view.
     */
    override fun getAnchorView(): View? = bindingNewPassword.btnResetPasswordSave

    /**
     * Called when a network request fails.
     *
     * @param reason The reason for the failure.
     */
    override fun onFailed(reason: String) {
        super.onFailed(reason)
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }

    abstract fun attachNewPasswordView(): ViewNewPasswordBinding

    open fun validateCurrentPassword() = true
}
