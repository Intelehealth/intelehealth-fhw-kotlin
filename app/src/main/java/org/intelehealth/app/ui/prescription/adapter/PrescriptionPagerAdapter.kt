package org.intelehealth.app.ui.prescription.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.ui.prescription.fragment.PrescriptionPendingFragment
import org.intelehealth.app.ui.prescription.fragment.PrescriptionReceiveFragment

class PrescriptionPagerAdapter(
    fragmentActivity: FragmentActivity,
) :
    FragmentStateAdapter(fragmentActivity) {
    var context: Context? = null
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PrescriptionReceiveFragment()
            else -> PrescriptionPendingFragment()

        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
