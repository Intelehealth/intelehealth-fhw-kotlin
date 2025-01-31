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
import org.intelehealth.common.extensions.hideError
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
    }

    private fun fetchLocation() {
        locationViewModel.getLocation().observe(viewLifecycleOwner, {
            it ?: return@observe
            locationViewModel.handleResponse(it) { result ->
                Timber.d { Gson().toJson(result) }
                result[KEY_RESULTS]?.let { locations -> bindLocation(locations) }
            }
        })

        binding.viewAuthenticationForm.autotvSelectLocation.setOnItemClickListener { _, _, position, _ ->
            locationAdapter.getItem(position)?.let { location ->
                locationViewModel.selectedLocation = location
                binding.viewAuthenticationForm.textInputLocation.hideError()
            }
        }
    }

    private fun bindLocation(locations: List<SetupLocation>) {
        Timber.d { Gson().toJson(locations) }
        locationAdapter = ArrayAdapterUtils.getObjectArrayAdapter(requireContext(), locations)
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

}