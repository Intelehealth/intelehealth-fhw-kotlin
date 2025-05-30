package org.intelehealth.app.ui.visit.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityOpenVisitsBinding
import org.intelehealth.app.ui.visit.adapter.OpenVisitRecyclerViewAdapter
import org.intelehealth.app.ui.visit.viewmodel.OpenVisitViewModel
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R

@AndroidEntryPoint
class OpenVisitsActivity : SimpleAppBarActivity() {

    private lateinit var binding: ActivityOpenVisitsBinding
    private val viewModel: OpenVisitViewModel by viewModels<OpenVisitViewModel>()

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.lbl_open_visits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, org.intelehealth.app.R.layout.activity_open_visits)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout.toolbar)
        binding.viewModel = viewModel
    }

    override fun onActionBarSet() {
        super.onActionBarSet()
        bindRecentOpenVisitsAdapter()
        bindOlderOpenVisitsAdapter()
    }

    private fun bindRecentOpenVisitsAdapter() {
        viewModel.recentVisitList.observe(this@OpenVisitsActivity) {
            it ?: return@observe
            val adapter = OpenVisitRecyclerViewAdapter(
                this@OpenVisitsActivity,
                it.toMutableList()
            )
            binding.progressBar.root.isVisible = false
            binding.recentView.recyclerVisit.setupLinearView(adapter, false)
            binding.recentView.visitTitle = getString(R.string.title_recent_visit)
        }
    }

    private fun bindOlderOpenVisitsAdapter() {
        viewModel.olderVisitList.observe(this@OpenVisitsActivity) {
            it ?: return@observe
            val adapter = OpenVisitRecyclerViewAdapter(
                this@OpenVisitsActivity,
                it.toMutableList()
            )
            binding.progressBar.root.isVisible = false
            binding.olderView.recyclerVisit.setupLinearView(adapter, false)
            binding.olderView.visitTitle = getString(R.string.title_older_visit)
        }
    }
}