package org.intelehealth.app.ui.visit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentVisitDetailsBinding
import org.intelehealth.common.ui.fragment.MenuFragment

/**
 * Created by Tanvir Hasan on 24-04-25
 * Email : mhasan@intelehealth.org
 **/

/**
 * VisitDetailsFragment: A Fragment responsible for displaying the detailed information of a specific visit.
 *
 * This Fragment fetches and presents comprehensive data related to a selected visit.
 * It could include information such as the date of the visit, the location, the purpose of the visit,
 * the person visited, notes taken during the visit, and any other relevant details.
 *
 * The layout for this Fragment (typically an XML file) would contain the UI elements
 * necessary to display this information. This might involve TextViews for text-based data,
 * ImageViews for visual data, and potentially other custom UI components.
 *
 * The data displayed in this fragment is expected to be provided through a navigation argument.
 * A user must have already selected a visit from a list of visits in order to navigate to this fragment.
 */
@AndroidEntryPoint
class VisitDetailsFragment : MenuFragment(R.layout.fragment_visit_details) {
    private lateinit var binding: FragmentVisitDetailsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVisitDetailsBinding.bind(view)
        setupClickListeners()
    }

    /**
     * Sets up click listeners for UI elements in this Fragment.
     *
     * Attaches actions to interactive views like buttons. Called in `onViewCreated()`.
     */
    private fun setupClickListeners() {
        binding.prescriptionView.prescriptionBlock.setOnClickListener {
           findNavController().navigate(VisitDetailsFragmentDirections.actionVisitDetailToPrescriptionDetail())
        }
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
        menuInflater.inflate(R.menu.menu_home, menu)
    }

    /**
     * Prepares the options menu for display.
     *
     * Called every time the menu is about to be shown. Use to dynamically update menu items.
     *
     * @param menu The menu to prepare.
     * @return True to display the menu, false otherwise.
     */
    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        menu.getItem(1).setVisible(false)
    }

    /**
     * Handles clicks on menu items.
     *
     * @param menuItem The selected menu item.
     * @return True if the click was handled, false otherwise.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

}
