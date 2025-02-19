package org.intelehealth.app.ui.achievement.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.R
import org.intelehealth.app.ui.achievement.fragment.AchievementDataFragment

/**
 * Created by Vaghela Mithun R. on 19-02-2025 - 18:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AchievementPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    context: Context
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = context.resources.getStringArray(R.array.tab_achievement_title)

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return AchievementDataFragment()
    }

    fun getTitle(position: Int) = fragments[position]
}
