package org.intelehealth.common.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.intelehealth.common.R
import org.intelehealth.common.databinding.FragmentCircularProgressbarBinding

/**
 * Created by Vaghela Mithun R. on 10-01-2025 - 12:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class CircularProgressFragment : Fragment(R.layout.fragment_circular_progressbar) {
    private val binding: FragmentCircularProgressbarBinding by lazy {
        FragmentCircularProgressbarBinding.bind(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressMax(100)
        onProgress(0)
        setupClickListeners()
        initiateProgressTask()
    }

    private fun setupClickListeners() {
        binding.btnRetryDownload.setOnClickListener {
            onRetry()
            binding.downloadErrorGroup.visibility = View.GONE
        }
        binding.btnClosePopup.setOnClickListener { onClose() }
    }

    fun progressTitle(title: String) {
        binding.txtProgressTitle.text = title
    }

    fun progressContent(content: String) {
        binding.txtProgressContent.text = content
    }

    fun errorMessage(message: String) {
        binding.txtErrorMsg.text = message
        binding.downloadErrorGroup.visibility = View.VISIBLE
    }

    fun onProgress(progress: Int) {
        requireActivity().runOnUiThread {
            binding.progressIndicator.progress = progress
            binding.txtProgress.text = "$progress%"
        }
    }

    fun progressMax(max: Int) {
        binding.progressIndicator.max = max
    }

    fun progressTask(status: String) {
        requireActivity().runOnUiThread {
            binding.txtProgressTask.text = status
        }
    }

    abstract fun initiateProgressTask()
    abstract fun onRetry()
    abstract fun onClose()
}