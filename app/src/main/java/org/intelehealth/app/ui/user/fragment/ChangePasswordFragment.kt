package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentChangePasswordBinding
import org.intelehealth.app.databinding.ViewNewPasswordBinding
import org.intelehealth.app.ui.onboarding.activity.OnboardingActivity
import org.intelehealth.common.extensions.gotoNextActivity
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that allows the user to change their password.
 *
 * This fragment extends [NewPasswordFragment] and provides the specific
 * implementation for changing the password, including UI setup, input
 * validation, and interaction with the [UserViewModel].
 */
class ChangePasswordFragment : NewPasswordFragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentChangePasswordBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        bindProgressView(binding.progressView)
        changeButtonStateOnTextChange()
    }

    /**
     * Enables or disables the save button based on text changes in the input
     * fields.
     *
     * This method disables the save button initially and enables it only when
     * the current password field has text. It also hides any error messages in
     * the current password field when the text changes.
     */
    private fun changeButtonStateOnTextChange() {
        binding.viewNewPassword.btnResetPasswordSave.isEnabled = false
        binding.textInputCurrentPassword.doOnTextChanged { _, _, _, _ ->
            changeSaveButtonState()
            binding.textInputLayoutCurrentPassword.hideError()
        }
    }

    override fun requestResetPassword() {
        val oldPassword = binding.textInputCurrentPassword.text.toString()
        val newPassword = binding.viewNewPassword.textInputNewPassword.text.toString()
        viewModel.changePassword(oldPassword, newPassword).observe(viewLifecycleOwner) { result ->
            viewModel.handleResponse(result) {
                viewModel.logout()
                alertForLogoutUserOnPasswordChanged()
            }
        }
    }

    /**
     * Displays an alert dialog to inform the user that they will be logged out
     * after changing their password.
     *
     * This dialog prompts the user to sign back in and navigates them to the
     * [OnboardingActivity] upon confirmation.
     */
    private fun alertForLogoutUserOnPasswordChanged() {
        DialogParams(
            icon = ResourceR.drawable.ic_dialog_alert,
            title = ResourceR.string.action_logout,
            message = ResourceR.string.content_recommend_after_password_change,
            positiveLbl = ResourceR.string.action_sign_back_in,
            negativeLbl = 0,
            onPositiveClick = {
                requireActivity().gotoNextActivity(OnboardingActivity::class.java, true)
            }
        ).let {
            showCommonDialog(it)
        }
    }

    override fun validateCurrentPassword(): Boolean {
        val oldPassword = binding.textInputCurrentPassword.text
        return oldPassword?.isNotEmpty() == true && oldPassword.length >= MIN_PASSWORD_LENGTH
    }

    override fun attachNewPasswordView(): ViewNewPasswordBinding = binding.viewNewPassword

    override fun onError(reason: String) {
        super.onError(reason)
        showErrorSnackBar(getAnchorView(), ResourceR.string.error_password_exists)
    }

    override fun getAnchorView(): View? = null
}
