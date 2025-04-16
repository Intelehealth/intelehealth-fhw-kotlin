package org.intelehealth.app.ui.user.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentLoginBinding
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showToast
import org.intelehealth.data.offline.entity.User
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:26.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that handles user login.
 *
 * This fragment extends [AuthenticationFragment] and provides the specific
 * implementation for user login, including UI setup, interaction with the
 * [UserViewModel] (through the base class), and navigation upon successful
 * authentication or when the user initiates the "forgot password" flow.
 */
@AndroidEntryPoint
class LoginFragment : AuthenticationFragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        bindAuthenticationForm(binding.viewAuthenticationForm)
        bindProgressView(binding.progressView)
        manageCollapsingLayout()
    }

    /**
     * Manages the behavior of the collapsing toolbar layout.
     *
     * This method sets up an [OnOffsetChangedListener] on the [AppBarLayout] to
     * change the title of the collapsing toolbar based on its collapsed/expanded
     * state. It also collapses the app bar when the username or password input
     * fields gain focus.
     */
    private fun manageCollapsingLayout() {
        val collapsingToolbarLayout = binding.layoutCollapsing
        val appBarLayout = binding.appBarLayout
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow: Boolean = true
            var scrollRange: Int = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = resources.getString(ResourceR.string.title_login)
                    isShow = true
                } else if (isShow) {
                    // Careful: there should be a space between the double quotes,
                    // otherwise it won't work.
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        })

        binding.viewAuthenticationForm.textInputPassword.setOnFocusChangeListener { _, focus ->
            if (focus) appBarLayout.setExpanded(false, true)
        }
        binding.viewAuthenticationForm.textInputUsername.setOnFocusChangeListener { _, focus ->
            if (focus) appBarLayout.setExpanded(false, true)
        }
    }

    override fun onUserAuthenticated(user: User) {
        val successMsg = getString(ResourceR.string.content_login_successful, user.displayName)
        showToast(successMsg)
        findNavController().navigate(LoginFragmentDirections.actionLoginToHome())
    }

    override fun onForgotPasswordNavDirection() = LoginFragmentDirections.actionLoginToForgotPassword()
}
