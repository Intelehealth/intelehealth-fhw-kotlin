package org.intelehealth.app.ui.setting.activity

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.databinding.ActivitySettingBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 18-03-2025 - 12:23.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SettingActivity : SimpleAppBarActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    override fun getScreenTitle(): String = getString(R.string.action_settings)
}
