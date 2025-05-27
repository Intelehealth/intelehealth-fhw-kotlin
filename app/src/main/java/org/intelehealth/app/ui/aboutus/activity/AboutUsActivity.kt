package org.intelehealth.app.ui.aboutus.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityAboutUsBinding
import org.intelehealth.app.ui.aboutus.adapter.AboutUsThumbAdapter
import org.intelehealth.app.utility.INTELEHEALTH_WEB_LINK
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.extensions.enableMovementMethod
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 17-03-2025 - 16:03.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Displays the "About Us" information for the application.
 *
 * This activity shows details about the app, including a description,
 * a link to the website, and potentially a list of images or thumbnails.
 * It extends [SimpleAppBarActivity] to provide a consistent app bar.
 */
@AndroidEntryPoint
class AboutUsActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        activeAutoLinkMethod()
        gotoWebsite()
        setThumbnailList()
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.action_about_us)

    /**
     * Enables clickable links (e.g., URLs, phone numbers) in the "About Us" text.
     */
    private fun activeAutoLinkMethod() {
        binding.contentViewAboutUs.tvAboutUsTermCondition.enableMovementMethod()
    }

    /**
     * Sets up a click listener for the "Go to Website" button.
     */
    private fun gotoWebsite() {
        binding.contentViewAboutUs.btnGoToWebsite.setOnClickListener {
            startIntelehealthWebsiteIntent()
        }
    }

    /**
     * Launches an intent to open the Intelehealth website in a browser.
     */
    private fun startIntelehealthWebsiteIntent() {
        Intent(Intent.ACTION_VIEW, Uri.parse(INTELEHEALTH_WEB_LINK)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    /**
     * Sets up the adapter and layout manager for the thumbnail list (if any).
     */
    private fun setThumbnailList() {
        AboutUsThumbAdapter(this, arrayListOf<Int>().apply {
            add(R.drawable.about_us_11) // Assuming R.drawable.about_us_11 is a placeholder
        }).apply {
            binding.contentViewAboutUs.rvAboutUsThumbnail.setupHorizontalLinearView(this)
        }
    }

    companion object {
        private const val INTELEHEALTH_WEB_LINK = "https://www.intelehealth.org/" // Replace with actual link if different
    }
}
