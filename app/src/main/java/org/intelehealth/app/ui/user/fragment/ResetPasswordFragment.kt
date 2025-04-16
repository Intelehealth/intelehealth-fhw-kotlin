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
class ResetPasswordFragment : NewPasswordFragment(R.layout.fragment_reset_password) {
    private lateinit var binding: FragmentResetPasswordBinding

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been restored in to the view.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentResetPasswordBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        bindProgressView(binding.progressView)
        handleButtonClick()
    }

    /**
     * Handles the click events for the buttons in the fragment.
     *
     * This function sets click listeners for the following buttons:
     * - `btnHelp`: Starts a WhatsApp intent to contact support.
     * - `toolbar` navigation icon: Pops the back stack.
     */
    private fun handleButtonClick() {
        binding.btnHelp.setOnClickListener {
            startWhatsappIntent(
                getString(ResourceR.string.content_support_mobile_no_1),
                getString(ResourceR.string.content_help_whatsapp_string_2)
            )
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }


    /**
     * Requests a password reset.
     *
     * This function calls [UserViewModel.resetPassword] to request a password
     * reset with the new password. It observes the result and shows a success
     * message or an error message accordingly.
     */
    override fun requestResetPassword() {
        viewModel.resetPassword(
            args.userUuid, binding.viewNewPassword.textInputNewPassword.text.toString()
        ).observe(viewLifecycleOwner, {
            it ?: return@observe
            viewModel.allowNullDataResponse(it) {
                showSuccessSnackBar(
                    binding.viewNewPassword.btnResetPasswordSave,
                    ResourceR.string.content_password_reset_successful
                )
                findNavController().popBackStack()
            }
        })
    }

    override fun attachNewPasswordView(): ViewNewPasswordBinding = binding.viewNewPassword
}
