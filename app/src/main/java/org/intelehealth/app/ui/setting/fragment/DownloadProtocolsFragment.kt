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

    private fun changeButtonStateOnTextChange() {
        binding.textInputLayoutLicenseKey.changeButtonStateOnTextChange(
            binding.textInputLicenseKey,
            binding.btnDownloadProtocols
        )
    }

    private fun handleLicenseKeyValidation() {
        viewModel.validateLicenseKeyStateData.observe(viewLifecycleOwner) {
            binding.tvDPLicenseKeyValidating.isVisible = false
            if (it) findNavController().navigate(
                DownloadProtocolsFragmentDirections.navDownloadProtocolsToDownloadProgress()
            )
        }
    }

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
