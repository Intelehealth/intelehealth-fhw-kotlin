package org.intelehealth.common.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.intelehealth.common.databinding.SimpleAppbarBinding

/**
 * Created by Vaghela Mithun R. on 18-03-2025 - 12:31.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class SimpleAppBarActivity : AppCompatActivity() {
    private lateinit var appBarBinding: SimpleAppbarBinding

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        appBarBinding = getAppBarBinding()
        setupActionBar()
    }

    private fun setupActionBar() {
        appBarBinding.toolbar.title = getScreenTitle()
        appBarBinding.toolbar.subtitle = ""
        setSupportActionBar(appBarBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        appBarBinding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    abstract fun getAppBarBinding(): SimpleAppbarBinding
    abstract fun getScreenTitle(): String
}
