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
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.show
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.network.model.request.OTP_FOR_PASSWORD
import org.intelehealth.data.network.model.request.OTP_FOR_USERNAME
import org.intelehealth.data.network.model.request.OtpRequestParam
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
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

        if (binding.ccSpinnerForgotPassword.selectedCountryCode == IND_COUNTRY_CODE) {
            binding.mobileLength = 10
        }
    }

    private fun changeContinueButtonStateOnTextChange() {
        binding.textInputFPUsername.doOnTextChanged { text, _, _, _ ->
            binding.btnForgotPasswordContinue.isEnabled = (text?.isNotEmpty() == true && text.length > 5)
        }
        binding.textInputMobileNumber.doOnTextChanged { text, _, _, _ ->
            val mobileLength = binding.mobileLength ?: 10
            val enable = (text?.isNotEmpty() == true && text.length == mobileLength)
            binding.btnForgotPasswordContinue.isEnabled = enable
        }

        binding.btnForgotPasswordContinue.setOnClickListener { requestOtp() }
    }

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

    private fun requestOtp() {
        OtpRequestParam(
            otpFor = if (args.requestFor == OTP_FOR_PASSWORD) OTP_FOR_PASSWORD else OTP_FOR_USERNAME,
            userName = binding.textInputFPUsername.text.toString(),
            phoneNumber = binding.textInputMobileNumber.text.toString(),
            countryCode = binding.ccSpinnerForgotPassword.selectedCountryCode.toInt()
        ).also {
            viewModel.requestOTP(it).observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                viewModel.handleUserResponse(result) { success ->
                    val message = success.message ?: getString(ResourceR.string.content_otp_sent_successfully)
                    showSuccessSnackBar(
                        binding.btnForgotPasswordContinue, message.mapWithResourceId(requireContext())
                    )
                    findNavController().navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordToOtpVerification(it)
                    )
                }
            }
        }

        binding.textInputFPUsername.text?.clear()
        binding.textInputMobileNumber.text?.clear()
        binding.btnForgotPasswordContinue.isEnabled = false
    }

    override fun getAnchorView(): View = binding.btnForgotPasswordContinue

    override fun onFailed(reason: String) {
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }
}