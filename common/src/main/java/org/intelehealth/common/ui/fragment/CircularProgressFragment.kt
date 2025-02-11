package org.intelehealth.common.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import org.intelehealth.common.R
import org.intelehealth.common.databinding.FragmentCircularProgressbarBinding
import org.intelehealth.common.ui.activity.CircularProgressActivity.Companion.MAX_PROGRESS

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 12:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class CircularProgressFragment : StateFragment(R.layout.fragment_circular_progressbar) {
    private val binding: FragmentCircularProgressbarBinding by lazy {
        FragmentCircularProgressbarBinding.bind(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressMax(MAX_PROGRESS)
        onProgress(0)
        setupClickListeners()
        initiateProgressTask()
    }

    private fun setupClickListeners() {
        binding.btnRetryDownload.setOnClickListener {
            onRetry()
            binding.btnRetryDownload.visibility = View.GONE
        }
    }

    fun progressTitle(title: String) {
        binding.txtProgressTitle.text = title
    }

    fun progressContent(content: String) {
        binding.txtProgressContent.text = content
    }

    fun errorMessage() {
        binding.btnRetryDownload.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun onProgress(progress: Int) {
        requireActivity().runOnUiThread {
            binding.progressIndicator.progress = progress
            binding.txtProgress.text = "$progress%"
        }
    }

    private fun progressMax(max: Int) {
        binding.progressIndicator.max = max
    }

    fun progressTask(status: String) {
        requireActivity().runOnUiThread {
            binding.txtProgressTask.text = status
        }
    }

    abstract fun initiateProgressTask()
    abstract fun onRetry()
}
