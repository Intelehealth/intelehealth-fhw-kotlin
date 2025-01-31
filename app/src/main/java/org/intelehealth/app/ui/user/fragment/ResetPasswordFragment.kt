package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentResetPasswordBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.app.ui.user.viewmodel.UserViewModel.Companion.PASSWORD_LEN
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.passwordMatchWithConfirmPassword
import org.intelehealth.common.extensions.setCompoundDrawableClick
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.extensions.showTooltip
import org.intelehealth.common.extensions.startWhatsappIntent
import org.intelehealth.common.extensions.validatePasswordPattern
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class ResetPasswordFragment : BaseProgressFragment(R.layout.fragment_reset_password) {
    private lateinit var binding: FragmentResetPasswordBinding
    override val viewModel by viewModels<UserViewModel>()
    private val args by navArgs<ResetPasswordFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)
        bindProgressView(binding.progressView)
        handleButtonClick()
        changeSaveButtonStateOnTextChange()
    }

    private fun generatePassword() {
        viewModel.generatePassword().apply {
            binding.textInputNewPassword.setText(this)
            binding.textInputConfirmPassword.setText(this)
            changeSaveButtonState()
        }
    }

    private fun handleButtonClick() {
        binding.btnHelp.setOnClickListener {
            startWhatsappIntent(
                getString(org.intelehealth.resource.R.string.content_support_mobile_no_1),
                getString(org.intelehealth.resource.R.string.content_help_whatsapp_string_2)
            )
        }

        binding.btnResetPasswordSave.setOnClickListener {
            validateFields {
                requestResetPassword()
            }
        }

        binding.tvGenerateNewPassword.setOnClickListener { generatePassword() }
        binding.tvGenerateNewPassword.setCompoundDrawableClick(Gravity.END) {
            it.showTooltip(ResourceR.string.content_generate_password_tooltip_text)
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun changeSaveButtonStateOnTextChange() {
        binding.textInputNewPassword.doOnTextChanged { text, _, _, _ ->
            changeSaveButtonState()
            binding.textInputLayoutNewPassword.hideError()
        }
        binding.textInputConfirmPassword.doOnTextChanged { text, _, _, _ ->
            changeSaveButtonState()
            binding.textInputLayoutConfirmPassword.hideError()
        }
    }

    private fun changeSaveButtonState() {
        val newPassword = binding.textInputNewPassword.text
        val confirmPassword = binding.textInputConfirmPassword.text
        val validNewPassword = newPassword?.isNotEmpty() == true && newPassword.length >= PASSWORD_LEN
        val validConfirmPassword = confirmPassword?.isNotEmpty() == true && confirmPassword.length >= PASSWORD_LEN
        binding.btnResetPasswordSave.isEnabled = validNewPassword.and(validConfirmPassword)
    }

    private fun validateFields(onValid: () -> Unit) {
        val validPassword = binding.textInputLayoutNewPassword.validatePasswordPattern(
            binding.textInputNewPassword, ResourceR.string.error_password_validation
        )

        val validConfirmPassword = binding.textInputLayoutConfirmPassword.passwordMatchWithConfirmPassword(
            binding.textInputNewPassword, binding.textInputConfirmPassword
        )

        if (validPassword.and(validConfirmPassword)) onValid()
    }

    private fun requestResetPassword() {
        viewModel.resetPassword(
            args.userUuid, binding.textInputNewPassword.text.toString()
        ).observe(viewLifecycleOwner, {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) {
                showSuccessSnackBar(binding.btnResetPasswordSave, ResourceR.string.content_password_reset_successful)
                findNavController().popBackStack(R.id.fragmentLoginScreen, true)
            }
        })
    }

    override fun getAnchorView(): View = binding.btnResetPasswordSave

    override fun onFailed(reason: String) {
        super.onFailed(reason)
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }
}