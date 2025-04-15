package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentForgotPasswordBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.app.utility.IND_COUNTRY_CODE
import org.intelehealth.app.utility.IND_MOBILE_LEN
import org.intelehealth.app.utility.PWD_MIN_LENGTH
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.show
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.data.network.model.request.OTP_FOR_PASSWORD
import org.intelehealth.data.network.model.request.OTP_FOR_USERNAME
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that allows the user to request a password reset or recover their
 * username.
 *
 * This fragment provides a UI for users who have forgotten their password or
 * username. It allows them to request an OTP (One-Time Password) via either
 * their username or mobile number. Upon successful OTP request, it navigates
 * to the [OtpVerificationFragment].
 */
@AndroidEntryPoint
class ForgotPasswordFragment : BaseProgressFragment(R.layout.fragment_forgot_password) {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val args by navArgs<ForgotPasswordFragmentArgs>()
    override val viewModel: UserViewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)
        bindProgressView(binding.progressView)
        changeViewTypeVisibilityOnButtonClick()
        binding.btnForgotPasswordContinue.isEnabled = false
        binding.btnUsername.isSelected = true
        changeContinueButtonStateOnTextChange()
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // TODO: Consider moving the country code and mobile length logic to a
        // more centralized location or using a library for phone number
        // formatting and validation.
        if (binding.ccSpinnerForgotPassword.selectedCountryCode == IND_COUNTRY_CODE) {
            binding.mobileLength = IND_MOBILE_LEN
        }
    }

    /**
     * Enables or disables the "Continue" button based on text changes in the
     * input fields.
     *
     * This method monitors the text in the username and mobile number fields.
     * The "Continue" button is enabled only when the relevant field (based on
     * the selected option) has valid input.
     */
    private fun changeContinueButtonStateOnTextChange() {
        binding.textInputFPUsername.doOnTextChanged { text, _, _, _ ->
            binding.btnForgotPasswordContinue.isEnabled =
                (text?.isNotEmpty() == true && text.length > PWD_MIN_LENGTH)
        }
        binding.textInputMobileNumber.doOnTextChanged { text, _, _, _ ->
            val mobileLength = binding.mobileLength ?: IND_MOBILE_LEN
            val enable = (text?.isNotEmpty() == true && text.length == mobileLength)
            binding.btnForgotPasswordContinue.isEnabled = enable
        }

        // TODO: Consider extracting this click listener into a separate
        // method for better organization.
        binding.btnForgotPasswordContinue.setOnClickListener { requestOtp() }
    }

    /**
     * Changes the visibility of the input fields based on whether the user
     * wants to use their username or mobile number.
     *
     * This method sets up click listeners for the "Username" and "Mobile
     * Number" buttons. When one is selected, the input field for the other is
     * hidden, and the "Continue" button's enabled state is updated.
     */
    private fun changeViewTypeVisibilityOnButtonClick() {
        binding.btnUsername.setOnClickListener {
            binding.groupForgotPasswordUsername.show()
            binding.groupFPMobileNumber.hide()
            binding.btnUsername.isSelected = true
            binding.btnMobileNumber.isSelected = false
            binding.textInputMobileNumber.text?.clear()
        }

        binding.btnMobileNumber.setOnClickListener {
            binding.groupForgotPasswordUsername.hide()
            binding.groupFPMobileNumber.show()
            binding.btnUsername.isSelected = false
            binding.btnMobileNumber.isSelected = true
            binding.textInputFPUsername.text?.clear()
        }
    }

    /**
     * Requests an OTP for password reset or username recovery.
     *
     * This method constructs an [OtpRequestParam] object with the appropriate
     * parameters based on the user's selection (username or mobile number) and
     * the purpose (password reset or username recovery). It then calls the
     * [UserViewModel] to request the OTP and observes the result. Upon success,
     * it displays a success message and navigates to the
     * [OtpVerificationFragment].
     */
    private fun requestOtp() {
        // TODO: Consider extracting the creation of OtpRequestParam into a
        // separate function for better readability.
        OtpRequestParam(
            otpFor = if (args.requestFor == OTP_FOR_PASSWORD) OTP_FOR_PASSWORD else OTP_FOR_USERNAME,
            userName = binding.textInputFPUsername.text.toString(),
            phoneNumber = binding.textInputMobileNumber.text.toString(),
            countryCode = binding.ccSpinnerForgotPassword.selectedCountryCode.toInt()
        ).also { requestParams ->
            viewModel.requestOTP(requestParams).observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                viewModel.handleUserResponse(result) { success ->
                    val message = success.message ?: getString(ResourceR.string.content_otp_sent_successfully)
                    showSuccessSnackBar(
                        binding.btnForgotPasswordContinue, message.mapWithResourceId(requireContext())
                    )
                    findNavController().navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordToOtpVerification(requestParams)
                    )
                }
            }
        }

        // TODO: Consider moving the clearing of text fields and disabling the
        // button to the success lambda above, to ensure it only happens after
        // a successful request.
        binding.textInputFPUsername.text?.clear()
        binding.textInputMobileNumber.text?.clear()
        binding.btnForgotPasswordContinue.isEnabled = false
    }

    override fun getAnchorView(): View = binding.btnForgotPasswordContinue

    override fun onFailed(reason: String) {
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }
}
