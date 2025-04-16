package org.intelehealth.app.ui.achievement.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.R
import org.intelehealth.app.ui.achievement.fragment.AchievementDailyFragment
import org.intelehealth.app.ui.achievement.fragment.AchievementDateRangeFragment
import org.intelehealth.app.ui.achievement.fragment.AchievementOverallFragment

/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 18:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Pager adapter for displaying achievement-related fragments in a ViewPager2.
 *
 * This adapter manages the different achievement views (Overall, Daily, Date Range)
 * as fragments within a ViewPager2. It uses a FragmentStateAdapter to efficiently
 * handle fragment lifecycle and state.
 *
 * @param fragmentManager The FragmentManager for the activity or fragment hosting the ViewPager2.
 * @param lifecycle The Lifecycle of the activity or fragment hosting the ViewPager2.
 * @param context The context in which the adapter is being used, used to access resources.
 */
class AchievementPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    context: Context
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = context.resources.getStringArray(R.array.tab_achievement_title)

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AchievementOverallFragment()
            1 -> AchievementDailyFragment()
            2 -> AchievementDateRangeFragment()
            else -> AchievementOverallFragment()
        }
    }

    /**
     * Returns the title for the fragment at the given position.
     *
     * @param position The position of the fragment in the ViewPager2.
     * @return The title string for the fragment.
     */
    fun getTitle(position: Int) = fragments[position]
}
