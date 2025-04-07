package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentChangePasswordBinding
import org.intelehealth.app.databinding.ViewNewPasswordBinding
import org.intelehealth.app.ui.onboarding.activity.OnboardingActivity
import org.intelehealth.common.extensions.gotoNextActivity
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.showAlertDialog
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.CommonConstants.MIN_PASSWORD_LENGTH
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChangePasswordFragment : NewPasswordFragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentChangePasswordBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        bindProgressView(binding.progressView)
        changeButtonStateOnTextChange()
    }

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
        viewModel.changePassword(oldPassword, newPassword).observe(viewLifecycleOwner) {
            viewModel.handleResponse(it) {
                viewModel.logout()
                alertForLogoutUserOnPasswordChanged()
            }
        }
    }

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
