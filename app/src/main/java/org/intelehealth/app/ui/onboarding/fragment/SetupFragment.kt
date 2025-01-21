package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSetupBinding
import org.intelehealth.app.ui.location.viewmodel.LocationViewModel
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.app.utility.KEY_RESULTS
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.showAlertDialog
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.extensions.showTooltipOnClick
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDropDown
import org.intelehealth.common.extensions.validatePassword
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.ArrayAdapterUtils
import org.intelehealth.data.network.model.JWTParams
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {
    private lateinit var binding: FragmentSetupBinding
    private val userViewModel: UserViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var locationAdapter: ArrayAdapter<SetupLocation>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)
        handleButtonClick()
        fetchLocation()
        handleInputError()
    }

    private fun handleInputError() {
        binding.textInputLayoutUsername.hideErrorOnTextChang(binding.textInputUsername)
        binding.textInputLayoutPassword.hideErrorOnTextChang(binding.textInputPassword)
    }

    private fun handleButtonClick() {
        binding.btnSetup.setOnClickListener {
            validateFields { generateJWTAuthToken() }
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(SetupFragmentDirections.actionSetupToForgotPassword())
        }

        binding.tvForgotUsername.setOnClickListener {
            showAlertDialog(
                DialogParams(
                    title = ResourceR.string.action_forgot_username,
                    message = ResourceR.string.content_contact_your_admin,
                )
            )
        }

        binding.setupInfoQuestionMark.showTooltipOnClick(ResourceR.string.content_enter_the_credentials)
    }


    private fun fetchLocation() {
        locationViewModel.getLocation().observe(viewLifecycleOwner, {
            it ?: return@observe
            userViewModel.handleResponse(it) { result ->
                Timber.d { Gson().toJson(result) }
                result[KEY_RESULTS]?.let { locations -> bindLocation(locations) }
            }
        })

        binding.autotvSelectLocation.setOnItemClickListener { _, _, position, _ ->
            locationAdapter.getItem(position)?.let { location ->
                locationViewModel.selectedLocation = location
                binding.textInputLocation.hideError()
            }
        }
    }

    private fun bindLocation(locations: List<SetupLocation>) {
        Timber.d { Gson().toJson(locations) }
        locationAdapter = ArrayAdapterUtils.getObjectArrayAdapter(requireContext(), locations)
        binding.autotvSelectLocation.setAdapter(locationAdapter)
        Timber.d { "Location Adapter set ${locationAdapter.count}" }
    }

    private fun validateFields(onValidated: () -> Unit) {
        val validLocation = binding.textInputLocation.validateDropDown(
            binding.autotvSelectLocation, ResourceR.string.error_location_not_selected
        )

        val validUsername = binding.textInputLayoutUsername.validate(
            binding.textInputUsername, ResourceR.string.error_field_required
        )

        val validPassword = binding.textInputLayoutPassword.validatePassword(
            binding.textInputPassword, ResourceR.string.error_field_required
        )

        if (validLocation && validUsername && validPassword) onValidated()
    }

    private fun generateJWTAuthToken() {
        JWTParams(
            username = binding.textInputUsername.text.toString(),
            password = binding.textInputPassword.text.toString(),
        ).apply {
            userViewModel.generateJWTAuthToken(this).observe(viewLifecycleOwner, {
                it ?: return@observe
                userViewModel.handleResponse(it) { token ->
                    userViewModel.saveJWTToken(token)
                    login(this.username, this.password)
                }
            })
        }
    }

    private fun login(username: String, password: String) {
        userViewModel.login(username, password).observe(viewLifecycleOwner, {
            it ?: return@observe
            userViewModel.handleResponse(it) { loginResponse ->
                locationViewModel.saveLocation()
                userViewModel.saveUser(loginResponse) {
                    val successMsg = getString(ResourceR.string.content_login_successful, it.displayName)
                    showToast(successMsg)
                    findNavController().navigate(SetupFragmentDirections.actionSetupToSync(it.displayName))
                }
            }
        })
    }
}