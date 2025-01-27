package org.intelehealth.app.ui.user.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ViewAuthenticationFormBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.showAlertDialog
import org.intelehealth.common.extensions.showTooltipOnClick
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDropDown
import org.intelehealth.common.extensions.validatePassword
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.network.model.request.JWTParams
import org.intelehealth.data.network.model.response.LoginResponse
import org.intelehealth.data.offline.entity.User
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 24-01-2025 - 11:08.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@AndroidEntryPoint
abstract class AuthenticationFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ViewAuthenticationFormBinding
    private lateinit var user: User

    fun bindAuthenticationForm(formBinding: ViewAuthenticationFormBinding) {
        this.binding = formBinding
        handleButtonClick()
        handleInputError()
        fetchUserIfLoggedIn()
    }

    private fun fetchUserIfLoggedIn() {
        if (binding.isLocationEnabled != null && binding.isLocationEnabled == false) return
        userViewModel.getUser().observe(viewLifecycleOwner) {
            it ?: return@observe
            user = it
        }
    }

    private fun handleInputError() {
        binding.textInputLayoutUsername.hideErrorOnTextChang(binding.textInputUsername)
        binding.textInputLayoutPassword.hideErrorOnTextChang(binding.textInputPassword)
    }

    private fun handleButtonClick() {
        binding.btnSetup.setOnClickListener {
            validateFields {
                val username = binding.textInputUsername.text.toString()
                val password = binding.textInputPassword.text.toString()
                generateJWTAuthToken(username, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(onForgotPasswordNavDirection())
        }

        binding.tvForgotUsername.setOnClickListener {
            showAlertDialog(
                DialogParams(
                    title = R.string.action_forgot_username,
                    message = R.string.content_contact_your_admin,
                )
            )
        }

        binding.ivAuthenticationHelp.showTooltipOnClick(R.string.content_enter_the_credentials)
    }

    private fun validateFields(onValidated: () -> Unit) {
        val validLocation = binding.isLocationEnabled?.let {
            if (it) binding.textInputLocation.validateDropDown(
                binding.autotvSelectLocation, R.string.error_location_not_selected
            ) else true
        } ?: true

        val validUsername = binding.textInputLayoutUsername.validate(
            binding.textInputUsername, R.string.error_field_required
        )

        val validPassword = binding.textInputLayoutPassword.validatePassword(
            binding.textInputPassword, R.string.error_field_required
        )

        if (validLocation && validUsername && validPassword) onValidated()
    }

    private fun generateJWTAuthToken(username: String, password: String) {
        JWTParams(username = username, password = password).apply {
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
            userViewModel.handleResponse(it) { loginResponse -> saveLoginResponse(loginResponse) }
        })
    }

    private fun saveLoginResponse(response: LoginResponse) {
        binding.isLocationEnabled?.let {
            if (it) userViewModel.saveUser(response) { user -> onUserAuthenticated(user) }
            else userViewModel.updateUser(user.apply {
                sessionId = response.sessionId
                lastLoginInTime = DateTimeUtils.getCurrentDateWithDBFormat()
            }) { onUserAuthenticated(user) }

        }
    }

    abstract fun onUserAuthenticated(user: User)
    abstract fun onForgotPasswordNavDirection(): NavDirections
}