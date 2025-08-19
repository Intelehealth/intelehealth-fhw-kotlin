package org.intelehealth.app.ui.visit.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityPrescriptionBinding
import org.intelehealth.app.databinding.ActivityVisitDetailBinding
import org.intelehealth.common.databinding.SimpleAppbarBinding
import org.intelehealth.common.ui.activity.SimpleAppBarActivity
import org.intelehealth.resource.R as resourceR

/**
 * Created by Tanvir Hasan on 2-04-25
 * Email : mhasan@intelehealth.org
 *
 * Handling received and pending fragment's prescriptions
 */
@AndroidEntryPoint
class VisitDetailActivity : SimpleAppBarActivity() {
    private val binding by lazy { ActivityVisitDetailBinding.inflate(layoutInflater) }
    private val args: VisitDetailActivityArgs by navArgs()

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostVisitDetails) as NavHostFragment
        navHostFragment.navController
    }

    /**
     * Returns the ViewBinding object for the screen's app bar.
     *
     * Provides access to the 'SimpleAppbarBinding' instance associated with this screen's layout,
     * allowing manipulation or access to views within the app bar.
     */
    override fun getAppBarBinding(): SimpleAppbarBinding = binding.appBarLayout

    /**
     * Returns the title for this screen.
     *
     * Retrieves the title string from resources, identified by 'R.string.title_prescriptions'.
     * This is likely used to set the ActionBar/Toolbar title.
     */
    override fun getScreenTitle(): String = getString(resourceR.string.title_visit_details)
    // Assuming 'resourceR' is a typo and should be 'R'


    /**
     * Called when the activity is first created.
     *
     * This is where you should do all of your normal static set up:
     * create views, bind data to lists, etc. This method also provides
     * a Bundle containing the activity's previously frozen state, if there was one.
     * Initializes the content view and sets up the Toolbar as the ActionBar.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Sets the activity's layout
        Timber.d { "Activity VisitId => ${args.visitId}" }
        navController.setGraph(R.navigation.visit_navigation_graph, Bundle().apply {
            putString(VISIT_ID, args.visitId)
        })
        setSupportActionBar(binding.appBarLayout.toolbar) // Sets the Toolbar as the support action bar
    }


    /**
     * Called when the ActionBar/Toolbar has been set up or is ready.
     *
     * This override calls the superclass implementation and then proceeds
     * to set up navigation-related components or listeners.
     */
    override fun onActionBarSet() {
        super.onActionBarSet()
        setupNavigation()
    }

    /**
     * Sets up the navigation controller and synchronizes the toolbar title with
     * the current destination.
     */
    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.appBarLayout.toolbar.title = destination.label
        }
    }

    companion object {
        const val VISIT_ID = "visitId" // Key for passing visit ID in intents
    }
}
