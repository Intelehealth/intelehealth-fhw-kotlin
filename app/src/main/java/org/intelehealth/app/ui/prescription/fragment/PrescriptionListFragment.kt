package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAyuPolicyBinding
import org.intelehealth.app.databinding.FragmentPrescriptionListBinding
import org.intelehealth.app.databinding.FragmentSetupBinding
import org.intelehealth.app.ui.prescription.adapter.PrescriptionPagerAdapter
import org.intelehealth.common.ui.fragment.MenuFragment

/**
 * Created by Tanvir Hasan on 2-04-25
 * Email : mhasan@intelehealth.org
 *
 * PrescriptionListFragment: Displays a list of prescriptions using a TabLayout and ViewPager.
 *
 * Uses tabs to separate "Received" and "Pending" prescriptions.
 */
class PrescriptionListFragment : MenuFragment(R.layout.fragment_prescription_list) {
    private lateinit var binding: FragmentPrescriptionListBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionListBinding.bind(view)
        setupTabAndViewPager()
    }

    /**
     * Sets up the TabLayout and ViewPager to display prescription lists.
     * Configures the ViewPager with an adapter and links it to the TabLayout for tabbed navigation.
     */
    private fun setupTabAndViewPager() {
        if (binding.prescriptionViewPager.adapter == null) {
            val adapter = PrescriptionPagerAdapter(requireActivity())
            binding.prescriptionViewPager.setAdapter(adapter)

            TabLayoutMediator(
                binding.prescriptionTabLayout, binding.prescriptionViewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.setText(
                    resources.getString(
                        if (position == 0) R.string.received else R.string.pending
                    )
                ).setIcon(org.intelehealth.resource.R.drawable.presc_tablayout_icon)
            }.attach()

            binding.prescriptionViewPager.setOffscreenPageLimit(1)
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