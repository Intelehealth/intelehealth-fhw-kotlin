package org.intelehealth.app.ui.user.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentUserProfileBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.app.utility.IND_COUNTRY_CODE
import org.intelehealth.app.utility.IND_MOBILE_LEN
import org.intelehealth.app.utility.OTHER_MOBILE_LEN
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.showSuccessSnackBar
import org.intelehealth.common.extensions.validateDigit
import org.intelehealth.common.extensions.validateEmail
import org.intelehealth.common.ui.fragment.ChangePhotoFragment
import org.intelehealth.common.utility.PathUtils
import org.intelehealth.data.network.model.request.UserProfileEditableDetails
import org.intelehealth.data.offline.entity.User
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 25-03-2025 - 17:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that allows the user to view and edit their profile information.
 *
 * This fragment displays the user's profile details, including their profile
 * picture, email address, and phone number. It allows the user to change their
 * profile picture, update their contact information, change their password, and
 * manage fingerprint authentication for the app.
 */
@AndroidEntryPoint
class UserProfileFragment : ChangePhotoFragment(R.layout.fragment_user_profile) {

    override val viewModel: UserViewModel by viewModels<UserViewModel>()
    private lateinit var binding: FragmentUserProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)
        bindProgressView(binding.progressView)
        observerUserData()
        handleClickEvent()
        hideErrorOnTextChanged()
        setMobileNumberLength()
        changeSaveButtonStateOnChanged()
        setFingerprintAppLockState()
    }

    /**
     * Enables or disables the "Save" button based on changes in the input
     * fields.
     *
     * This method monitors changes in the email address, phone number, and
     * country code fields. The "Save" button is enabled only when there are
     * unsaved changes compared to the user's current profile information.
     */
    private fun changeSaveButtonStateOnChanged() {
        binding.textInputUserEmail.doOnTextChanged { _, _, _, _ -> activeButtonOnDetailChanged() }
        binding.textInputUserPhoneNumber.doOnTextChanged { _, _, _, _ -> activeButtonOnDetailChanged() }
        binding.ccpUserCountryCode.setOnCountryChangeListener { activeButtonOnDetailChanged() }
    }

    /**
     * Sets the state of the fingerprint authentication controls based on
     * device capabilities and the user's current settings.
     *
     * This method checks if biometric authentication is available on the
     * device. If it is, it enables the fingerprint lock switch and sets its
     * initial state based on the user's preference. It also sets up a listener
     * to update the user's preference when the switch is toggled.
     */
    private fun setFingerprintAppLockState() {
        val isActive = viewModel.isBiometricAvailable()
        binding.btnSetFingerprintLock.isEnabled = isActive
        binding.switchFingerprintLock.isEnabled = isActive
        binding.switchFingerprintLock.isChecked = viewModel.fingerprintAppLock.value ?: false
        binding.switchFingerprintLock.setOnCheckedChangeListener { _, b ->
            viewModel.changeFingerprintAppLockState(b)
        }
    }

    /**
     * Sets the maximum length of the mobile number input field based on the
     * selected country code.
     *
     * This method checks the selected country code and sets the `mobileLength`
     * property of the binding to the appropriate maximum length for mobile
     * numbers in that country.
     */
    private fun setMobileNumberLength() {
        val maxLength = if (binding.ccpUserCountryCode.selectedCountryCode == IND_COUNTRY_CODE) {
            IND_MOBILE_LEN
        } else OTHER_MOBILE_LEN

        binding.mobileLength = maxLength
    }

    /**
     * Handles click events for various buttons in the fragment.
     *
     * This method sets up click listeners for:
     * - The "Change Photo" button, which triggers the profile picture selection
     *   process.
     * - The "Save" button, which validates the form and saves the updated
     *   profile information.
     * - The "Change Password" button, which navigates to the change password
     *   fragment.
     */
    private fun handleClickEvent() {
        binding.btnChangePhoto.setOnClickListener { requestPermission() }
        binding.btnSaveUserProfile.setOnClickListener { validateForm { saveProfile() } }
        binding.btnChangePassword.setOnClickListener {
            val direction = UserProfileFragmentDirections.navProfileToChangePassword()
            findNavController().navigate(direction)
        }
    }

    /**
     * Hides error messages in the input fields when the text changes.
     *
     * This method sets up listeners to hide error messages in the email address
     * and phone number input fields when the user types in them.
     */
    private fun hideErrorOnTextChanged() {
        binding.textInputLayoutUserEmail.hideErrorOnTextChang(binding.textInputUserEmail)
        binding.textInputLayoutUserPhoneNumber.hideErrorOnTextChang(binding.textInputUserPhoneNumber)
    }

    /**
     * Validates the user input in the profile editing form.
     *
     * This method checks the validity of the email address and phone number
     * fields. If both fields are valid, it invokes the provided [onValid]
     * lambda.
     *
     * @param onValid A lambda to be executed if all input fields are valid.
     */
    private fun validateForm(onValid: () -> Unit) {
        // separate functions for better readability and maintainability.
        val error = getString(ResourceR.string.error_invalid_mobile_number, binding.mobileLength)
        val validNumber = binding.textInputLayoutUserPhoneNumber.validateDigit(
            binding.textInputUserPhoneNumber, error, IND_MOBILE_LEN
        )

        val validEmail = binding.textInputLayoutUserEmail.validateEmail(
            binding.textInputUserEmail, ResourceR.string.error_invalid_email
        )

        if (validEmail.and(validNumber)) onValid()
    }

    /**
     * Observes the user's profile data from the [UserViewModel].
     *
     * This method observes two LiveData objects from the [UserViewModel]:
     * - The result of fetching the user profile (currently only logs the status).
     * - The user's profile information, which is used to populate the UI
     *   fields. It also sets the initial country code in the country code picker
     *   based on the user's data.
     */
    private fun observerUserData() {
        // more explicitly, e.g., by displaying loading indicators or error
        // messages.
        viewModel.fetchUserProfile().observe(viewLifecycleOwner) { Timber.d { it.status.name } }
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            binding.user = user
            val countryCode: Int = user.countryCode?.let { return@let it.toInt() } ?: IND_COUNTRY_CODE.toInt()
            binding.ccpUserCountryCode.setCountryForPhoneCode(countryCode)
        }
    }

    /**
     * Handles the result of the profile picture selection.
     *
     * This method is called when a new profile picture is selected. It updates
     * the profile picture in the UI and enables the "Save" button.
     *
     * @param uri The URI of the selected profile picture.
     */
    override fun onProfilePictureSelected(uri: Uri) {
        Timber.d { "Profile => $uri" }
        binding.ivUserProfilePicture.tag = uri
        Glide.with(requireContext())
            .load(uri.toString())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(ResourceR.drawable.img_avatar)
            .placeholder(ResourceR.drawable.img_avatar)
            .into(binding.ivUserProfilePicture)
        activeButtonOnDetailChanged()
    }

    /**
     * Updates the state of the "Save" button based on changes in the profile
     * details.
     *
     * This method checks if any of the profile details (profile picture, email
     * address, phone number, or country code) have changed compared to the
     * current user's data. If any changes are detected, it enables the "Save"
     * button.
     */
    private fun activeButtonOnDetailChanged() {
        val image = binding.ivUserProfilePicture.tag
        val phoneNumber = binding.textInputUserPhoneNumber.text
        val email = binding.textInputUserEmail.text
        val countryCode = binding.ccpUserCountryCode.selectedCountryCode
        val user = binding.user

        val isActive = if (image != null) true
        else if (phoneNumber != null && phoneNumber.toString() != user?.phoneNumber) true
        else if (email != null && email.toString() != user?.emailId) true
        else if (countryCode != user?.countryCode) true
        else false

        binding.btnSaveUserProfile.isEnabled = isActive
    }

    /**
     * Saves the user's profile information.
     *
     * This method is called when the user clicks the "Save" button. It
     * disables the button to prevent multiple clicks and creates a
     * [UserProfileEditableDetails] object with the updated profile information.
     * It then calls [updateProfile] to update the user's profile.
     */
    private fun saveProfile() {
        binding.btnSaveUserProfile.isEnabled = false
        UserProfileEditableDetails(
            profilePicture = binding.ivUserProfilePicture.tag?.toString(),
            email = binding.textInputUserEmail.text?.toString(),
            countryCode = binding.ccpUserCountryCode.selectedCountryCode,
            phoneNumber = binding.textInputUserPhoneNumber.text?.toString()
        ).apply { binding.user?.let { updateProfile(it, this) } }
    }

    /**
     * Updates the user's profile information.
     *
     * This method checks if a new profile picture has been selected. If so, it
     * retrieves the file path and updates the [UserProfileEditableDetails]
     * object. It then calls [requestUpdateProfile] to send the updated
     * information to the server.
     *
     * @param user The current user object.
     * @param editableDetails The updated profile details.
     */
    private fun updateProfile(user: User, editableDetails: UserProfileEditableDetails) {
        if (editableDetails.profilePicture != null) {
            PathUtils(requireContext()).getPath(binding.ivUserProfilePicture.tag as Uri).apply {
                Timber.d { "Path => $this" }
                editableDetails.profilePicture = this
            }.also { requestUpdateProfile(user, editableDetails) }
        } else requestUpdateProfile(user, editableDetails)
    }

    /**
     * Sends the updated profile information to the server.
     *
     * This method calls the [UserViewModel] to update the user's profile on
     * the server. It also updates the local user object with the new profile
     * information and shows a success message.
     *
     * @param user The current user object.
     * @param editableDetails The updated profile details.
     */
    private fun requestUpdateProfile(user: User, editableDetails: UserProfileEditableDetails) {
        viewModel.updateUserProfile(user, editableDetails) {
            user.profileVersion = System.currentTimeMillis()
            user.emailId = editableDetails.email
            user.countryCode = editableDetails.countryCode
            user.phoneNumber = editableDetails.phoneNumber
            updateUserLocalProfile(user)
        }
    }

    /**
     * Updates the local user object with the new profile information.
     *
     * This method calls the [UserViewModel] to update the local user object
     * with the new profile information. It also shows a success message to the
     * user.
     *
     * @param user The updated user object.
     */
    private fun updateUserLocalProfile(user: User) {
        viewModel.updateUser(user) {
            binding.ivUserProfilePicture.tag = null
            showSuccessSnackBar(binding.btnSaveUserProfile, ResourceR.string.content_profile_updated_successfully)
        }
    }

    /**
     * Handles the result of a permission request.
     *
     * This method is called when the user grants or denies the permission to
     * access the device's storage. If the permission is granted, it opens the
     * image picker to select a new profile picture.
     *
     * @param requestCode The request code for the permission.
     * @param permissions The requested permissions.
     * @param grantResults The results of the permission requests.
     */
    override fun retryOnNetworkLost() {
        super.retryOnNetworkLost()
        binding.btnSaveUserProfile.performClick()
    }

    /**
     * Returns the anchor view for the tooltip.
     *
     * This method returns the "Save" button as the anchor view for the
     * tooltip.
     *
     * @return The "Save" button view.
     */
    override fun getAnchorView(): View = binding.btnSaveUserProfile
}
