package org.intelehealth.app.ui.onboarding.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSetupBinding
import org.intelehealth.app.ui.location.viewmodel.LocationViewModel
import org.intelehealth.app.ui.user.fragment.AuthenticationFragment
import org.intelehealth.app.utility.KEY_RESULTS
import org.intelehealth.common.extensions.getSpinnerItemAdapter
import org.intelehealth.common.extensions.hideError
import org.intelehealth.common.extensions.showNetworkLostSnackBar
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.utility.ArrayAdapterUtils
import org.intelehealth.data.network.model.SetupLocation
import org.intelehealth.data.offline.entity.User
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that handles user setup, including location selection and
 * authentication.
 *
 * This fragment allows the user to select their location from a list and
 * completes the setup process by authenticating the user. It interacts with
 * the [LocationViewModel] to fetch and save location data.
 */
@AndroidEntryPoint
class SetupFragment : AuthenticationFragment(R.layout.fragment_setup) {
    private lateinit var binding: FragmentSetupBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var locationAdapter: ArrayAdapter<SetupLocation>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)
        bindAuthenticationForm(binding.viewAuthenticationForm)
        bindProgressView(binding.progressView)
        fetchLocation()
        handleLostNetwork()
    }

    /**
     * Observes the network connection status and displays a snackbar if the
     * connection is lost.
     *
     * The snackbar provides an option to retry the operation when the network
     * is available again.
     */
    private fun handleLostNetwork() {
        locationViewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (!it) showNetworkLostSnackBar(
                binding.viewAuthenticationForm.root, ResourceR.string.error_could_not_connect_with_server
            ) { retryOnNetworkLost() }
        }
    }

    /**
     * Fetches the available locations and binds them to the location selection
     * dropdown.
     *
     * This method observes the location data from the [LocationViewModel],
     * processes the response, and populates the location dropdown with the
     * retrieved locations. It also sets up a listener to handle location
     * selection changes.
     */
    private fun fetchLocation() {
        locationViewModel.getLocation().observe(viewLifecycleOwner) {
            it ?: return@observe
            locationViewModel.handleResponse(it) { result ->
                Timber.d { Gson().toJson(result) }
                result[KEY_RESULTS]?.let { locations -> bindLocation(locations) }
            }
        }

        binding.viewAuthenticationForm.autotvSelectLocation.setOnItemClickListener { _, _, position, _ ->
            locationAdapter.getItem(position)?.let { location ->
                locationViewModel.selectedLocation = location
                binding.viewAuthenticationForm.textInputLocation.hideError()
            }
        }
    }

    /**
     * Binds the list of locations to the location selection dropdown.
     *
     * This method creates an [ArrayAdapter] from the provided list of
     * [SetupLocation] objects and sets it as the adapter for the dropdown.
     *
     * @param locations The list of locations to display in the dropdown.
     */
    private fun bindLocation(locations: List<SetupLocation>) {
        Timber.d { Gson().toJson(locations) }
        locationAdapter = requireContext().getSpinnerItemAdapter(locations)
        binding.viewAuthenticationForm.autotvSelectLocation.setAdapter(locationAdapter)
        Timber.d { "Location Adapter set ${locationAdapter.count}" }
    }

    override fun onUserAuthenticated(user: User) {
        locationViewModel.saveLocation()
        val successMsg = getString(ResourceR.string.content_setup_successful, user.displayName)
        showToast(successMsg)
        findNavController().navigate(SetupFragmentDirections.actionSetupToSync(user.displayName))
    }

    override fun onForgotPasswordNavDirection() = SetupFragmentDirections.actionSetupToForgotPassword()

    override fun retryOnNetworkLost() {
        super.retryOnNetworkLost()
        fetchLocation()
    }

}
