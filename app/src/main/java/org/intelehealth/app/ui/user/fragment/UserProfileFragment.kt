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

    private fun changeSaveButtonStateOnChanged() {
        binding.textInputUserEmail.doOnTextChanged { _, _, _, _ -> activeButtonOnDetailChanged() }
        binding.textInputUserPhoneNumber.doOnTextChanged { _, _, _, _ -> activeButtonOnDetailChanged() }
        binding.ccpUserCountryCode.setOnCountryChangeListener { activeButtonOnDetailChanged() }
    }

    private fun setFingerprintAppLockState() {
        val isActive = viewModel.isBiometricAvailable()
        binding.btnSetFingerprintLock.isEnabled = isActive
        binding.switchFingerprintLock.isEnabled = isActive
        binding.switchFingerprintLock.isChecked = viewModel.fingerprintAppLock.value ?: false
        binding.switchFingerprintLock.setOnCheckedChangeListener { _, b ->
            viewModel.changeFingerprintAppLockState(b)
        }
    }

    private fun setMobileNumberLength() {
        val maxLength = if (binding.ccpUserCountryCode.selectedCountryCode == IND_COUNTRY_CODE) {
            IND_MOBILE_LEN
        } else OTHER_MOBILE_LEN

        binding.mobileLength = maxLength
    }

    private fun handleClickEvent() {
        binding.btnChangePhoto.setOnClickListener { requestPermission() }
        binding.btnSaveUserProfile.setOnClickListener { validateForm { saveProfile() } }
        binding.btnChangePassword.setOnClickListener {
            val direction = UserProfileFragmentDirections.navProfileToChangePassword()
            findNavController().navigate(direction)
        }
    }

    private fun hideErrorOnTextChanged() {
        binding.textInputLayoutUserEmail.hideErrorOnTextChang(binding.textInputUserEmail)
        binding.textInputLayoutUserPhoneNumber.hideErrorOnTextChang(binding.textInputUserPhoneNumber)
    }

    private fun validateForm(onValid: () -> Unit) {
        val error = getString(ResourceR.string.error_invalid_mobile_number, binding.mobileLength)
        val validNumber = binding.textInputLayoutUserPhoneNumber.validateDigit(
            binding.textInputUserPhoneNumber, error, IND_MOBILE_LEN
        )

        val validEmail = binding.textInputLayoutUserEmail.validateEmail(
            binding.textInputUserEmail, ResourceR.string.error_invalid_email
        )

        if (validEmail.and(validNumber)) onValid()
    }

    private fun observerUserData() {
        viewModel.fetchUserProfile().observe(viewLifecycleOwner) { Timber.d { it.status.name } }
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            binding.user = user
            val countryCode: Int = user.countryCode?.let { return@let it.toInt() } ?: IND_COUNTRY_CODE.toInt()
            binding.ccpUserCountryCode.setCountryForPhoneCode(countryCode)
        }
    }

    override fun onProfilePictureSelected(uri: Uri) {
        Timber.d { "Profile => $uri" }
        binding.ivUserProfilePicture.tag = uri
        Glide.with(requireContext())
            .load(uri.toString())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(ResourceR.drawable.avatar1)
            .placeholder(ResourceR.drawable.avatar1)
            .into(binding.ivUserProfilePicture)
        activeButtonOnDetailChanged()
    }

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

    private fun saveProfile() {
        binding.btnSaveUserProfile.isEnabled = false
        UserProfileEditableDetails(
            profilePicture = binding.ivUserProfilePicture.tag?.toString(),
            email = binding.textInputUserEmail.text?.toString(),
            countryCode = binding.ccpUserCountryCode.selectedCountryCode,
            phoneNumber = binding.textInputUserPhoneNumber.text?.toString()
        ).apply { binding.user?.let { updateProfile(it, this) } }
    }

    private fun updateProfile(user: User, editableDetails: UserProfileEditableDetails) {
        if (editableDetails.profilePicture != null) {
            PathUtils(requireContext()).getPath(binding.ivUserProfilePicture.tag as Uri).apply {
                Timber.d { "Path => $this" }
                editableDetails.profilePicture = this
            }.also { requestUpdateProfile(user, editableDetails) }
        } else requestUpdateProfile(user, editableDetails)
    }

    private fun requestUpdateProfile(user: User, editableDetails: UserProfileEditableDetails) {
        viewModel.updateUserProfile(user, editableDetails) {
            user.profileVersion = System.currentTimeMillis()
            user.emailId = editableDetails.email
            user.countryCode = editableDetails.countryCode
            user.phoneNumber = editableDetails.phoneNumber
            updateUserLocalProfile(user)
        }
    }

    private fun updateUserLocalProfile(user: User) {
        viewModel.updateUser(user) {
            binding.ivUserProfilePicture.tag = null
            showSuccessSnackBar(binding.btnSaveUserProfile, ResourceR.string.content_profile_updated_successfully)
        }
    }

    override fun retryOnNetworkLost() {
        super.retryOnNetworkLost()
        binding.btnSaveUserProfile.performClick()
    }

    override fun getAnchorView(): View = binding.btnSaveUserProfile
}
