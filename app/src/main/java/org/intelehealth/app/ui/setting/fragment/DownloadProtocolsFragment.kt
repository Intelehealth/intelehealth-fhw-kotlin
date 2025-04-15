package org.intelehealth.app.ui.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentDownloadProtocolsBinding
import org.intelehealth.app.ui.setting.viewmodel.SettingViewModel
import org.intelehealth.common.extensions.changeButtonStateOnTextChange
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.ui.fragment.StateFragment

/**
 * Created by Vaghela Mithun R. on 19-03-2025 - 12:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that allows the user to download new protocols by entering a
 * license key.
 *
 * This fragment provides a UI for entering a license key and initiating the
 * protocol download process. It interacts with the [SettingViewModel] to
 * validate the license key and start the download.
 */
@AndroidEntryPoint
class DownloadProtocolsFragment : StateFragment(R.layout.fragment_download_protocols) {
    private lateinit var binding: FragmentDownloadProtocolsBinding
    override val viewModel by activityViewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadProtocolsBinding.bind(view)
        binding.btnDownloadProtocols.isEnabled = false
        changeButtonStateOnTextChange()
        handleButtonClick()
        handleLicenseKeyValidation()
    }

    /**
     * Enables or disables the download button based on the text entered in the
     * license key field.
     *
     * This method uses an extension function to monitor changes in the license
     * key text field and update the enabled state of the download button
     * accordingly.
     */
    private fun changeButtonStateOnTextChange() {
        binding.textInputLayoutLicenseKey.changeButtonStateOnTextChange(
            binding.textInputLicenseKey,
            binding.btnDownloadProtocols
        )
    }

    /**
     * Observes the license key validation state and navigates to the download
     * progress screen if the key is valid.
     *
     * This method observes a LiveData from the [SettingViewModel] that indicates
     * whether the entered license key is valid. If the key is valid, it
     * navigates to the [DownloadProtocolProgressFragment].
     */
    private fun handleLicenseKeyValidation() {
        viewModel.validateLicenseKeyStateData.observe(viewLifecycleOwner) { isValid ->
            binding.tvDPLicenseKeyValidating.isVisible = false
            if (isValid) findNavController().navigate(
                DownloadProtocolsFragmentDirections.navDownloadProtocolsToDownloadProgress()
            )
        }
    }

    /**
     * Handles the click event for the download button.
     *
     * This method retrieves the entered license key, disables the button to
     * prevent multiple clicks, displays a validating message, and calls the
     * [SettingViewModel] to start the protocol download process with the
     * provided license key. It also clears the license key text field.
     */
    private fun handleButtonClick() {
        binding.btnDownloadProtocols.setOnClickListener {
            it ?: return@setOnClickListener
            it.isEnabled = false
            val licenseKey = binding.textInputLicenseKey.text.toString()
            binding.tvDPLicenseKeyValidating.isVisible = true
            viewModel.startDownloadProtocolWorker(licenseKey)
            binding.textInputLicenseKey.text?.clear()
        }
    }

    override fun onFailed(reason: String) {
        super.onFailed(reason)
        binding.tvDPLicenseKeyValidating.isVisible = false
        showErrorSnackBar(message = reason)
    }
}
