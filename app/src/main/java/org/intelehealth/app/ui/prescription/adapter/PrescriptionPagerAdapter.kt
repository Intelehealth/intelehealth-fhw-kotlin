package org.intelehealth.app.ui.prescription.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.R
import org.intelehealth.app.ui.prescription.fragment.PrescriptionListFragment
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.offline.entity.PrescriptionStatusCount

/**
 * Created by Tanvir Hasan on 2-04-25
 * Email : mhasan@intelehealth.org
 *
 * PrescriptionViewHolder: Manages a single prescription item view in a RecyclerView.
 * Binds patient data to the item layout and handles click events.
 */
class PrescriptionPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val prescriptionStatusCount: PrescriptionStatusCount
) : FragmentStateAdapter(fragmentActivity) {

    /**
     * Retrieves the tab titles from the string array resource R.array.tab_prescription_title.
     * These titles will be used to label the tabs in the TabLayout.
     */
    private val fragments = fragmentActivity.resources.getStringArray(R.array.tab_prescription_title)

    /**
     * Creates and returns a new Fragment instance based on the given position.
     * This method determines which Fragment to instantiate for each tab in the ViewPager.
     * Params:
     * position - The position of the tab for which to create a Fragment.
     * Returns:
     * A new Fragment instance, either PrescriptionReceiveFragment (for position 0) or PrescriptionPendingFragment (for any other position).
     */
    override fun createFragment(position: Int): Fragment {
        return PrescriptionListFragment.newInstance(
            VisitDetail.TabType.entries[position],
            prescriptionStatusCount
        )
    }

    /**
     * Returns the total number of items in the ViewPager.
     * In this case, there are always two tabs.
     * Returns:
     * The number of tabs (always 2).
     */
    override fun getItemCount(): Int {
        return fragments.size
    }

    /**
     * Returns the title for the fragment at the given position.
     *
     * @param position The position of the fragment in the ViewPager2.
     * @return The title string for the fragment.
     */
    fun getTitle(position: Int): String = fragments[position]
}
