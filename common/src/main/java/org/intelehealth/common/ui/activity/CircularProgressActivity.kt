package org.intelehealth.common.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.intelehealth.common.databinding.ActivityCircularProgressbarBinding
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS

/**
 * Created by Vaghela Mithun R. on 11-12-2024 - 11:09.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class CircularProgressActivity : AppCompatActivity() {
    protected val binding: ActivityCircularProgressbarBinding by lazy {
        ActivityCircularProgressbarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressMax(MAX_PROGRESS)
        onProgress(0)
        onViewCreated()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRetryDownload.setOnClickListener { onRetry() }
        binding.btnClosePopup.setOnClickListener { onClose() }
    }

    abstract fun onViewCreated()
    abstract fun onRetry()
    abstract fun onClose()

    fun progressTitle(title: String) {
        binding.txtProgressTitle.text = title
    }

    fun errorMessage(message: String) {
        binding.txtErrorMsg.text = message
    }

    @SuppressLint("SetTextI18n")
    fun onProgress(progress: Int) {
        runOnUiThread {
            binding.progressIndicator.progress = progress
            binding.txtProgress.text = "$progress%"
        }
    }

    fun progressMax(max: Int) {
        binding.progressIndicator.max = max
    }

    fun progressTask(status: String) {
        binding.txtProgressTask.text = status
    }
}
