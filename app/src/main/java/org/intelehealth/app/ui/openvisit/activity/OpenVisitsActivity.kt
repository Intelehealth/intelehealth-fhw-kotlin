package org.intelehealth.app.ui.openvisit.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivityOpenVisitsBinding
import org.intelehealth.app.ui.openvisit.adapter.OpenVisitRecyclerViewAdapter
import org.intelehealth.app.ui.openvisit.viewmodel.OpenVisitViewModel
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.state.Result
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R

@AndroidEntryPoint
class OpenVisitsActivity : SimpleAppBarActivity() {

    private val binding by lazy { ActivityOpenVisitsBinding.inflate(layoutInflater) }
    private val viewModel: OpenVisitViewModel by viewModels<OpenVisitViewModel>()

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.lbl_open_visits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout.toolbar)
        binding.viewModel = viewModel
    }

    override fun onActionBarSet() {
        super.onActionBarSet()
        bindOpenVisitsAdapter()
    }

    private fun bindOpenVisitsAdapter() {
        viewModel.recentVisitList.observe(this@OpenVisitsActivity) {
            it ?: return@observe
            val adapter = OpenVisitRecyclerViewAdapter(
                this@OpenVisitsActivity,
                it.toMutableList()
            )
            binding.recentView.recyclerRecent.setupLinearView(adapter, false)
            binding.progressBar.root.isVisible = false
        }
    }
}