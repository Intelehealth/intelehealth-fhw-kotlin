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

/**
 * A Fragment that handles the OTP (One-Time Password) verification process.
 *
 * This fragment allows users to enter a six-digit OTP, verifies the entered OTP,
 * and handles the OTP countdown timer and resend functionality.
 * It also provides a help button to contact support via WhatsApp.
 *
 * The fragment uses view binding for layout access, navigation components for screen transitions,
 * and a [UserViewModel] for managing user-related data and operations.
 *
 * @property binding The view binding for the fragment's layout.
 * @property args The navigation arguments passed to the fragment, including the OTP request model.
 * @property viewModel The ViewModel responsible for managing user-related data and operations.
 */
@AndroidEntryPoint
class OTPVerificationFragment : BaseProgressFragment(R.layout.fragment_otp_verification) {

    private lateinit var binding: FragmentOtpVerificationBinding
    private val args by navArgs<OTPVerificationFragmentArgs>()
    override val viewModel by viewModels<UserViewModel>()

    /**
     * Called when the fragment's view has been created.
     *
     * Initializes the view binding, sets up the progress view, disables the continue button initially,
     * observes the OTP countdown, starts the countdown timer, adds text change listeners to the OTP input fields,
     * and sets up click listeners for the buttons.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
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

    /**
     * Sets up click listeners for the toolbar navigation, continue button, and help button.
     *
     * - The toolbar navigation button goes back to the previous screen.
     * - The continue button sends the entered OTP for verification.
     * - The help button opens a WhatsApp chat with the support number.
     */
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

    /**
     * Sets up the key listener for an OTP input field.
     *
     * This function handles the backspace key press to move the focus to the previous input field
     * if the current field is empty.
     *
     * @param currentEditText The current OTP input field.
     */
    private fun setupOtpKeyHandler(currentEditText: TextInputEditText) {
        currentEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentEditText.text.isNullOrEmpty()) {
                currentEditText.clearFocus()
                currentEditText.previousFocus()
                true
            } else false
        }
    }

    /**
     * Adds text change listeners to all OTP input fields.
     *
     * This function sets up the OTP input fields and their respective listeners to handle
     * text input and focus changes.
     */
    private fun addTextChangeListener() {
        val otpView = binding.viewOtpInputBox
        setupOtpInput(otpView.etPin1, otpView.etPin2)
        setupOtpInput(otpView.etPin2, otpView.etPin3)
        setupOtpInput(otpView.etPin3, otpView.etPin4)
        setupOtpInput(otpView.etPin4, otpView.etPin5)
        setupOtpInput(otpView.etPin5, otpView.etPin6)
        setupOtpInput(otpView.etPin6, null)
    }

    /**
     * Sets up an OTP input field with a text change listener and key listener.
     *
     * This function configures a single OTP input field to move focus to the next field
     * when a digit is entered and to validate the OTP and activate the send button.
     *
     * @param currentEditText The current OTP input field.
     * @param nextEditText The next OTP input field, or null if this is the last field.
     */
    private fun setupOtpInput(currentEditText: TextInputEditText, nextEditText: TextInputEditText?) {
        currentEditText.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) nextEditText?.requestFocus()
            validateAndActiveSendButton()
        }
        setupOtpKeyHandler(currentEditText)
    }

    /**
     * Validates the OTP input and activates the send button if the OTP is complete.
     *
     * This function checks if all OTP input fields are filled and enables the continue button
     * if the OTP is complete, otherwise it disables the button.
     */
    private fun validateAndActiveSendButton() {
        val otpView = binding.viewOtpInputBox
        otpView.etPin1.text.isNullOrEmpty().or(otpView.etPin2.text.isNullOrEmpty())
            .or(otpView.etPin3.text.isNullOrEmpty()).or(otpView.etPin4.text.isNullOrEmpty())
            .or(otpView.etPin5.text.isNullOrEmpty()).or(otpView.etPin6.text.isNullOrEmpty()).let {
                binding.btnVerifyOtpContinue.isEnabled = !it
            }
    }

    /**
     * Retrieves the entered OTP from the input fields.
     *
     * @return The six-digit OTP as a string.
     */
    private fun getOtpFromView(): String {
        val otpView = binding.viewOtpInputBox
        return otpView.etPin1.text.toString() + otpView.etPin2.text.toString() + otpView.etPin3.text.toString() + otpView.etPin4.text.toString() + otpView.etPin5.text.toString() + otpView.etPin6.text.toString()
    }

    /**
     * Handles the resend OTP functionality.
     *
     * This function sets up the click listener for the resend OTP text view, which requests a new OTP
     * and starts the countdown timer again.
     */
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

    /**
     * Observes the OTP countdown timer and updates the resend OTP text view accordingly.
     *
     * This function observes the [UserViewModel]'s `otpCountDownLiveData` and updates the
     * `tvResendOtp` text view to show the remaining time or the resend action.
     */
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

    /**
     * Sends the entered OTP to the server for verification.
     *
     * This function retrieves the OTP from the input fields, sets it in the request model,
     * and calls the [UserViewModel]'s `verifyOTP` function to send the OTP to the server.
     * It observes the response and, upon successful verification, navigates to the reset password screen.
     *
     * If the verification is successful, it extracts the user ID from the response, shows a success
     * message, and navigates to the reset password screen, passing the user ID as a parameter.
     */
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

    /**
     * Provides the anchor view for displaying Snackbars.
     *
     * This function is used by the [BaseProgressFragment] to determine the view to which
     * Snackbars should be anchored. In this fragment, the "Verify OTP Continue" button is used
     * as the anchor view. This ensures that Snackbars are displayed above the button and do not
     * obscure other important UI elements.
     *
     * @return The anchor view for displaying Snackbars, which is the "Verify OTP Continue" button.
     */
    override fun getAnchorView(): View = binding.btnVerifyOtpContinue

    /**
     * Handles the failure case when an operation fails.
     *
     * This function is called when an operation, such as OTP verification, fails.
     * It displays an error Snackbar to the user, providing feedback about the failure.
     * The error message is mapped to a resource ID using the `mapWithResourceId` function,
     * allowing for localized error messages.
     *
     * @param reason The reason for the failure, which will be displayed in the error Snackbar.
     */
    override fun onFailed(reason: String) {
        showErrorSnackBar(getAnchorView(), reason.mapWithResourceId(requireContext()))
    }
}
