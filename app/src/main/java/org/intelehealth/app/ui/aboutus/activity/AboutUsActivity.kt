package org.intelehealth.app.ui.aboutus.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.intelehealth.app.databinding.ActivityAboutUsBinding
import org.intelehealth.app.ui.aboutus.adapter.AboutUsThumbAdapter
import org.intelehealth.app.utility.INTELEHEALTH_WEB_LINK
import org.intelehealth.common.extensions.enableMovementMethod
import org.intelehealth.common.extensions.setupHorizontalLinearView
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 17-03-2025 - 16:03.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        activeAutoLinkMethod()
        gotoWebsite()
        setThumbnailList()
    }

    private fun setupActionBar() {
        binding.appBarLayout.toolbar.title = getString(ResourceR.string.action_about_us)
        binding.appBarLayout.toolbar.subtitle = ""
        setSupportActionBar(binding.appBarLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun activeAutoLinkMethod() {
        binding.contentViewAboutUs.tvAboutUsTermCondition.enableMovementMethod()
    }

    private fun gotoWebsite() {
        binding.contentViewAboutUs.btnGoToWebsite.setOnClickListener {
            startIntelehealthWebsiteIntent()
        }
    }

    private fun startIntelehealthWebsiteIntent() {
        Intent(Intent.ACTION_VIEW, Uri.parse(INTELEHEALTH_WEB_LINK)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun setThumbnailList() {
        AboutUsThumbAdapter(this, arrayListOf<Int>().apply {
            add(ResourceR.drawable.about_us_11)
        }).apply {
            binding.contentViewAboutUs.rvAboutUsThumbnail.setupHorizontalLinearView(this)
        }
    }
}
