package org.intelehealth.app.ui.openvisit.activity

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityOpenVisitsBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R

@AndroidEntryPoint
class OpenVisitsActivity : SimpleAppBarActivity() {

    private val binding by lazy { ActivityOpenVisitsBinding.inflate(layoutInflater) }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.lbl_open_visits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout.toolbar)
    }

    override fun onActionBarSet() {
        super.onActionBarSet()
        setupNavigation()
    }

    private fun setupNavigation() {

    }
}