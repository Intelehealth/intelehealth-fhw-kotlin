package org.intelehealth.app.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.model.IntroContent
import org.intelehealth.app.model.ViewType
import org.intelehealth.app.ui.onboarding.fragment.IntroSlideFragment

/**
 * Created by Vaghela Mithun R. on 07-01-2025 - 18:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A [FragmentStateAdapter] for displaying introduction slides in a ViewPager2.
 *
 * This adapter manages the fragments for the introduction slides, providing
 * the correct fragment for each position in the ViewPager2. It uses the
 * [IntroContent] data class to retrieve the content for each slide and
 * [IntroSlideFragment] to display it.
 *
 * @param activity The [FragmentActivity] hosting the ViewPager2.
 */
class IntroPagerAdapter(private val activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ViewType.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        val content = IntroContent.getContent(activity, ViewType.entries[position])
        return IntroSlideFragment.newInstance(content)
    }
}
