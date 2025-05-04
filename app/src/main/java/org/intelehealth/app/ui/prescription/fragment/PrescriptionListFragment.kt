package org.intelehealth.app.ui.prescription.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.intelehealth.app.R
import org.intelehealth.resource.R as resourceR
import org.intelehealth.app.databinding.FragmentPrescriptionListBinding
import org.intelehealth.app.ui.achievement.adapter.AchievementPagerAdapter
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
        bindViewPager()
    }

    /**
     * Sets up the TabLayout and ViewPager to display prescription lists.
     * Configures the ViewPager with an adapter and links it to the TabLayout for tabbed navigation.
     */
    private fun bindViewPager() {
        val adapter = PrescriptionPagerAdapter(requireActivity())
        binding.prescriptionViewPager.adapter = adapter
        TabLayoutMediator(binding.prescriptionTabLayout, binding.prescriptionViewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
            tab.icon = ContextCompat.getDrawable(requireActivity(), resourceR.drawable.ic_presc_tablayout)
        }.attach()
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
