package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOtpVerificationBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.mapWithResourceId
import org.intelehealth.common.extensions.previousFocus
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.extensions.startWhatsappIntent
import org.intelehealth.common.ui.fragment.BaseProgressFragment
import org.intelehealth.data.provider.user.UserDataSource.Companion.KEY_USER_UUID
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
    override val viewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpVerificationBinding.bind(view)
        bindProgressView(binding.progressView)
        binding.btnVerifyOtpContinue.isEnabled = false
        observeOTPCountDown()
        viewModel.startOTPCountDownTimer()
        addTextChangeListener()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnVerifyOtpContinue.setOnClickListener { sendOtpToVerify() }

        binding.btnHelp.setOnClickListener {
            startWhatsappIntent(
                getString(ResourceR.string.content_support_mobile_no_1),
                getString(ResourceR.string.content_help_whatsapp_string_2)
            )
        }
    }

    private fun setupOtpKeyHandler(currentEditText: TextInputEditText) {
        currentEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentEditText.text.isNullOrEmpty()) {
                currentEditText.clearFocus()
                currentEditText.previousFocus()
                true
            } else false
        }
    }

    private fun addTextChangeListener() {
        val otpView = binding.viewOtpInputBox
        setupOtpInput(otpView.etPin1, otpView.etPin2)
        setupOtpInput(otpView.etPin2, otpView.etPin3)
        setupOtpInput(otpView.etPin3, otpView.etPin4)
        setupOtpInput(otpView.etPin4, otpView.etPin5)
        setupOtpInput(otpView.etPin5, otpView.etPin6)
        setupOtpInput(otpView.etPin6, null)
    }

    private fun setupOtpInput(currentEditText: TextInputEditText, nextEditText: TextInputEditText?) {
        currentEditText.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) nextEditText?.requestFocus()
            validateAndActiveSendButton()
        }
        setupOtpKeyHandler(currentEditText)
        setupOtpKeyHandler(currentEditText)
        setupOtpKeyHandler(currentEditText)
        setupOtpKeyHandler(currentEditText)
        setupOtpKeyHandler(currentEditText)
        setupOtpKeyHandler(currentEditText)
    }

    private fun validateAndActiveSendButton() {
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
            viewModel.requestOTP(args.reqModel).observe(viewLifecycleOwner) {
                it ?: return@observe
                viewModel.handleUserResponse(it) { success ->
                    val message = success.message ?: getString(ResourceR.string.content_otp_sent_successfully)
                    showSuccessSnackBar(
                        binding.btnVerifyOtpContinue, message.mapWithResourceId(requireContext())
                    )
                    viewModel.startOTPCountDownTimer()
                }
            }
        }
    }

    private fun observeOTPCountDown() {
        viewModel.otpCountDownLiveData.observe(viewLifecycleOwner) {
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

    private fun sendOtpToVerify() {
        args.reqModel.otp = getOtpFromView()
        viewModel.verifyOTP(args.reqModel).observe(viewLifecycleOwner) {
            it ?: return@observe
            viewModel.handleResponse(it) { success ->
                success[KEY_USER_UUID]?.let { userId ->
                    showSuccessSnackBar(
                        binding.btnVerifyOtpContinue, getString(ResourceR.string.content_otp_verified_successfully)
                    )
                    findNavController().navigate(
                        OTPVerificationFragmentDirections.actionOtpVerificationToResetPassword(userId)
                    )
                }
            }
        }
    }

    override fun getAnchorView(): View = binding.btnVerifyOtpContinue

    override fun onFailed(reason: String) {
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }
}