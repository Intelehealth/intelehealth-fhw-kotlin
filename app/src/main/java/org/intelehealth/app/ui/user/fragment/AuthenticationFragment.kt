package org.intelehealth.app.ui.user.fragment

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ViewAuthenticationFormBinding
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.hideErrorOnTextChang
import org.intelehealth.common.extensions.setCompoundDrawableClick
import org.intelehealth.common.extensions.showAlertDialog
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showTooltip
import org.intelehealth.common.extensions.showTooltipOnClick
import org.intelehealth.common.extensions.validate
import org.intelehealth.common.extensions.validateDropDown
import org.intelehealth.common.extensions.validatePassword
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.ui.fragment.BaseProgressFragment
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

/**
 * An abstract base fragment for handling user authentication.
 *
 * This fragment provides a common implementation for authentication-related
 * functionality, including:
 * - Binding to an authentication form.
 * - Handling button clicks for login and navigation.
 * - Validating user input.
 * - Generating and saving JWT authentication tokens.
 * - Performing user login.
 * - Saving login responses.
 * - Displaying error messages.
 *
 * Subclasses must implement the [onUserAuthenticated] and
 * [onForgotPasswordNavDirection] methods to handle successful authentication
 * and navigation to the "forgot password" screen, respectively.
 *
 * @property layoutResId The layout resource ID for the fragment.
 */
@AndroidEntryPoint
abstract class AuthenticationFragment(@LayoutRes layoutResId: Int) : BaseProgressFragment(layoutResId) {
    override val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ViewAuthenticationFormBinding
    private lateinit var user: User

    /**
     * Binds the fragment to an authentication form.
     *
     * This method initializes the binding, sets up button click listeners,
     * handles input error display, and fetches the user if already logged in.
     *
     * @param formBinding The binding object for the authentication form.
     */
    fun bindAuthenticationForm(formBinding: ViewAuthenticationFormBinding) {
        this.binding = formBinding
        handleButtonClick()
        handleInputError()
        fetchUserIfLoggedIn()
    }

    /**
     * Fetches the user information if a location is not required or if it is
     * enabled.
     *
     * If location is not required (indicated by `isLocationEnabled` being
     * `null`) or if location is enabled, this method observes the user data
     * from the [UserViewModel] and stores it in the `user` property.
     */
    private fun fetchUserIfLoggedIn() {
        // TODO: Consider renaming `isLocationEnabled` to something more
        // descriptive, as the double negative can be confusing.  Also,
        // consider making this a non-nullable Boolean with a default value.
        if (binding.isLocationEnabled != null && binding.isLocationEnabled == false) return
        viewModel.getUser().observe(viewLifecycleOwner) {
            it ?: return@observe
            user = it
        }
    }

    /**
     * Handles the display of input errors.
     *
     * This method sets up listeners to hide error messages in the username and
     * password input fields when the text changes.
     */
    private fun handleInputError() {
        binding.textInputLayoutUsername.hideErrorOnTextChang(binding.textInputUsername)
        binding.textInputLayoutPassword.hideErrorOnTextChang(binding.textInputPassword)
    }

    /**
     * Handles button click events within the authentication form.
     *
     * This method sets up click listeners for:
     * - The main authentication button (`btnSetup`), which triggers field
     *   validation and authentication.
     * - The "Forgot Password" text view, which navigates to the forgot password
     *   screen.
     * - The "Forgot Username" text view, which displays an alert dialog with
     *   instructions.
     * - An optional instruction details text view, which shows a tooltip with
     *   additional information.
     */
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

        binding.tvInstructionDetails.setCompoundDrawableClick(Gravity.END) {
            it.showTooltip(R.string.content_enter_the_credentials)
        }
    }

    /**
     * Validates the input fields in the authentication form.
     *
     * This method checks the validity of the location (if applicable),
     * username, and password fields. If all fields are valid, it invokes the
     * provided [onValidated] lambda.
     *
     * @param onValidated A lambda to be executed if all fields are valid.
     */
    private fun validateFields(onValidated: () -> Unit) {
        // TODO: Consider extracting the validation logic for each field into
        // separate functions for better readability and maintainability.
        val validLocation = binding.isLocationEnabled?.let { isLocationEnabled ->
            if (isLocationEnabled) binding.textInputLocation.validateDropDown(
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

    /**
     * Generates a JWT authentication token.
     *
     * This method creates a [JWTParams] object with the provided username and
     * password, then calls the [UserViewModel] to generate a JWT token. It
     * observes the result and, upon success, saves the token and proceeds with
     * the login process.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    private fun generateJWTAuthToken(username: String, password: String) {
        JWTParams(username = username, password = password).apply {
            viewModel.generateJWTAuthToken(this).observe(viewLifecycleOwner) { result ->
                result ?: return@observe
                viewModel.handleResponse(result) { token ->
                    viewModel.saveJWTToken(token)
                    login(this.username, this.password)
                }
            }
        }
    }

    /**
     * Performs user login.
     *
     * This method calls the [UserViewModel] to perform the login operation
     * with the provided username and password. It observes the result and,
     * upon success, saves the login response.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    private fun login(username: String, password: String) {
        viewModel.login(username, password).observe(viewLifecycleOwner, {
            it ?: return@observe
            viewModel.handleResponse(it) { loginResponse -> saveLoginResponse(loginResponse) }
        })
    }

    /**
     * Saves the login response.
     *
     * This method checks if location is enabled and saves the user data
     * accordingly. If location is not enabled, it updates the user's session ID
     * and last login time.
     *
     * @param response The login response containing user data.
     */
    private fun saveLoginResponse(response: LoginResponse) {
        binding.isLocationEnabled?.let {
            if (it) viewModel.saveUser(response) { user -> onUserAuthenticated(user) }
            else viewModel.updateUser(user.apply {
                sessionId = response.sessionId
                lastLoginInTime = DateTimeUtils.getCurrentDateWithDBFormat()
            }) { onUserAuthenticated(user) }
        }
    }

    /**
     * Displays a tooltip with additional information.
     *
     * This method shows a tooltip on the "Instruction Details" text view when
     * clicked. The tooltip provides information about entering credentials.
     */
    override fun getAnchorView(): View = binding.btnSetup

    /**
     * Displays an error message when authentication fails.
     *
     * This method clears the password input field and shows an error snack
     * bar with a message indicating that the password is incorrect.
     *
     * @param reason The reason for the authentication failure.
     */
    override fun onFailed(reason: String) {
        super.onFailed(reason)
        binding.textInputPassword.text?.clear()
        showErrorSnackBar(message = R.string.error_incorrect_password)
    }

    /**
     * Displays an error message when the user is not found.
     *
     * This method clears the username and password input fields and shows an
     * error snack bar with a message indicating that the user was not found.
     *
     * @param reason The reason for the user not being found.
     */
    abstract fun onUserAuthenticated(user: User)

    /**
     * Navigates to the "Forgot Password" screen.
     *
     * This method returns a [NavDirections] object that specifies the
     * navigation action to be taken when the "Forgot Password" link is
     * clicked.
     *
     * @return The navigation direction for the "Forgot Password" screen.
     */
    abstract fun onForgotPasswordNavDirection(): NavDirections
}
