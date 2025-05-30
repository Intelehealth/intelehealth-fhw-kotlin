package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPrescriptionDetailsBinding
import org.intelehealth.common.ui.fragment.MenuFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.ui.prescription.viewmodel.PrescriptionDetailsViewModel

/**
 * Created by Tanvir Hasan on 2-04-25
 * Email : mhasan@intelehealth.org
 *
 *
 * PrescriptionDetailsFragment: Displays detailed information for a specific prescription.
 *
 * This Fragment is responsible for presenting the details of a prescription to the user.
 * It uses View Binding for efficient view access and implements the MenuProvider
 * interface to integrate with the options menu system.
 */
@AndroidEntryPoint
class PrescriptionDetailsFragment : MenuFragment(R.layout.fragment_prescription_details) {
    private lateinit var binding: FragmentPrescriptionDetailsBinding
    val viewModel: PrescriptionDetailsViewModel by viewModels<PrescriptionDetailsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionDetailsBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    /**
     * Initializes the Activity's options menu.
     *
     * Called once when the menu is first displayed. Use `onPrepareOptionsMenu` for later updates.
     *
     * @param menu The menu to inflate.
     * @return True if the menu should be displayed, false otherwise.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_prescription_details, menu)
    }

    /**
     * Prepares the options menu for display.
     *
     * Called every time the menu is about to be shown. Use to dynamically update menu items.
     *
     * @param menu The menu to prepare.
     * @return True to display the menu, false otherwise.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        return when (menuItem.itemId) {
            R.id.action_more -> {
                viewModel.updateOptionsVisibility()
                true
            }

            else ->  false
        }
    }

}
