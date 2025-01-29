package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOtpVerificationBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class OTPVerificationFragment : BaseProgressFragment(R.layout.fragment_otp_verification) {
    private lateinit var binding: FragmentOtpVerificationBinding
    private val args by navArgs<OTPVerificationFragmentArgs>()
    private val userViewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpVerificationBinding.bind(view)
        bindProgressView(binding.progressView)
        binding.btnVerifyOtpContinue.isEnabled = false
        observeOTPCountDown()
        userViewModel.startOTPCountDownTimer()
        observeOtherState()
        addTextChangeListener()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnVerifyOtpContinue.setOnClickListener { sendOtpToVerify() }
    }

    private fun addTextChangeListener() {
        val otpView = binding.viewOtpInputBox
        otpView.etPin1.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
        otpView.etPin2.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
        otpView.etPin3.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
        otpView.etPin4.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
        otpView.etPin5.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
        otpView.etPin6.doOnTextChanged { _, _, _, _ -> validateAndSendOtp() }
    }

    private fun validateAndSendOtp() {
        val otpView = binding.viewOtpInputBox
        otpView.etPin1.text.isNullOrEmpty().or(otpView.etPin2.text.isNullOrEmpty())
            .or(otpView.etPin3.text.isNullOrEmpty()).or(otpView.etPin4.text.isNullOrEmpty())
            .or(otpView.etPin5.text.isNullOrEmpty()).or(otpView.etPin6.text.isNullOrEmpty()).let {
                binding.btnVerifyOtpContinue.isEnabled = !it
            }
    }

    private fun getOtpFromView(): String {
        val otpView = binding.viewOtpInputBox
        return otpView.etPin1.text.toString() + otpView.etPin2.text.toString() + otpView.etPin3.text.toString() + otpView.etPin4.text.toString() + otpView.etPin5.text.toString() + otpView.etPin6.text.toString()
    }

    private fun handleResendOtp() {
        binding.tvResendOtp.setOnClickListener {
            userViewModel.requestOTP(args.reqModel).observe(viewLifecycleOwner) {
                it ?: return@observe
                userViewModel.handleUserResponse(it) { success ->
                    val message = success.message ?: getString(ResourceR.string.content_otp_sent_successfully)
                    showSuccessSnackBar(
                        binding.btnVerifyOtpContinue, message.mapWithResourceId(requireContext())
                    )
                    userViewModel.startOTPCountDownTimer()
                }
            }
        }
    }

    private fun observeOTPCountDown() {
        userViewModel.otpCountDownLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            val resendContent = if (it == 0L) {
                handleResendOtp()
                getString(ResourceR.string.action_resend_otp)
            } else {
                binding.tvResendOtp.setOnClickListener(null)
                getString(ResourceR.string.content_resend_otp_in, it)
            }
            binding.tvResendOtp.text = resendContent
        }
    }

    private fun observeOtherState() {
        userViewModel.failDataResult.observe(viewLifecycleOwner) {
            showErrorSnackBar(binding.btnVerifyOtpContinue, it.mapWithResourceId(requireContext()))
        }

        userViewModel.errorDataResult.observe(viewLifecycleOwner) {
            showErrorSnackBar(binding.btnVerifyOtpContinue, ResourceR.string.content_something_went_wrong)
        }

        userViewModel.loading.observe(viewLifecycleOwner) {
            if (it) showProgress() else hideProgress()
        }

        userViewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
            if (!it) {
                showErrorSnackBar(binding.btnVerifyOtpContinue, ResourceR.string.error_could_not_connect_with_server)
            }
        }
    }

    private fun sendOtpToVerify() {
        args.reqModel.otp = getOtpFromView()
        userViewModel.verifyOTP(args.reqModel).observe(viewLifecycleOwner) {
            it ?: return@observe
            userViewModel.handleResponse(it) { success ->
                findNavController().navigate(OTPVerificationFragmentDirections.actionOtpVerificationToResetPassword())
            }
        }
    }
}