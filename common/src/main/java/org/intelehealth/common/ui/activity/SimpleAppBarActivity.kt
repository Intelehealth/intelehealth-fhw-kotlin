package org.intelehealth.common.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.databinding.SimpleAppbarBinding

/**
 * Created by Vaghela Mithun R. on 18-03-2025 - 12:31.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * An abstract base activity that provides a simple app bar with a title and navigation button.
 *
 * This activity is designed to be extended by other activities that require a consistent
 * app bar layout. It handles the setup of the action bar and provides methods for
 * subclasses to customize the title and binding.
 */
abstract class SimpleAppBarActivity : AppCompatActivity() {
    private lateinit var appBarBinding: SimpleAppbarBinding

    /**
     * This method is called when the activity is created.
     * It sets up the action bar and initializes the app bar binding.
     *
     * @param savedInstanceState The saved instance state, if available.
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        appBarBinding = getAppBarBinding()
        setupActionBar()
    }

    /**
     * Sets up the action bar with the title and navigation button.
     * This method is called after the activity's view has been created.
     */
    private fun setupActionBar() {
        appBarBinding.toolbar.title = getScreenTitle()
        appBarBinding.toolbar.subtitle = ""
        setSupportActionBar(appBarBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        appBarBinding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onActionBarSet()
    }

    /**
     * This method should be implemented by subclasses to provide the
     * [SimpleAppbarBinding] instance for the activity.
     *
     * @return The [SimpleAppbarBinding] instance for the activity.
     */
    abstract fun getAppBarBinding(): SimpleAppbarBinding
    /**
     * This method should be implemented by subclasses to provide the
     * title for the action bar.
     *
     * @return The title for the action bar.
     */
    abstract fun getScreenTitle(): String

    /**
     * This method is called after the action bar has been set up.
     * You can override this method to perform additional actions
     * after the action bar is set.
     */
    open fun onActionBarSet() {
        Timber.d { "onActionBarSet" }
    }
}
