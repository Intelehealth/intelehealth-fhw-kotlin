package org.intelehealth.app.ui.home.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.ajalt.timberkt.Timber
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ActivityHomeBinding
import org.intelehealth.app.databinding.DrawerHomeNavHeaderBinding
import org.intelehealth.app.ui.home.fragment.HomeFragmentDirections
import org.intelehealth.app.ui.onboarding.activity.OnboardingActivity
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.gotoNextActivity
import org.intelehealth.common.extensions.imageSpan
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.ui.activity.BaseStatusBarActivity
import org.intelehealth.common.utility.ImageSpanGravity
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.config.presenter.feature.viewmodel.ActiveFeatureStatusViewModel
import org.intelehealth.data.network.model.SetupLocation
import javax.inject.Inject
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 03-01-2025 - 18:34.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * The main activity of the application, serving as the entry point after successful login.
 *
 * This activity hosts the main navigation structure, including a navigation drawer,
 * bottom navigation, and a toolbar. It manages user authentication state, displays
 * user information in the navigation drawer header, and handles navigation events.
 */
@AndroidEntryPoint
class HomeActivity : BaseStatusBarActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Inject preference utils
    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    private val userViewModel: UserViewModel by viewModels()
    private val afsViewModel: ActiveFeatureStatusViewModel by viewModels()
    private lateinit var headerBinding: DrawerHomeNavHeaderBinding

    // Binding for the activity
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    // Navigation controller for the activity
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHome) as NavHostFragment
        navHostFragment.navController
    }

    // AppBarConfiguration for the activity
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.contentViewMain.appBarLayout.toolbar)
        setupNavigation()
        setupHeaderView()
        observeUser()
        handleBackPressEvent()
        updateDeviceToken()
    }

    /**
     * Sets up the navigation controller with the toolbar, drawer layout, and bottom navigation view.
     *
     * This method configures the [NavController] with the [AppBarConfiguration] to
     * manage the interaction between the toolbar, drawer, and navigation graph.
     * It also sets up listeners for navigation events in the drawer and bottom navigation.
     */
    private fun setupNavigation() {
        // Create an AppBarConfiguration with the correct top-level destinations
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        val toolbar = binding.contentViewMain.appBarLayout.toolbar
        // Hook your navigation controller to toolbar
        toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener(this)
        // Hook your navigation controller to bottom navigation view
        binding.contentViewMain.bottomNavHome.setupWithNavController(navController)
        changeTitleAsDestinationChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Changes the title and subtitle of the toolbar based on the current navigation destination.
     *
     * This method listens for changes in the navigation destination and updates the
     * toolbar's title and subtitle accordingly.  For the home destination, it calls
     * [displayHomeTitle] to show the location and last sync time.
     */
    private fun changeTitleAsDestinationChanged() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> displayHomeTitle()
                else -> supportActionBar?.subtitle = null
            }
        }
    }

    /**
     * Displays the home screen title, including the user's location and last sync time.
     *
     * This method retrieves the user's location from preferences and displays it as
     * the toolbar title, along with an icon. It also calls [showLastSyncTime] to
     * display the last data synchronization time as the toolbar subtitle.
     */
    private fun displayHomeTitle() {
        preferenceUtils.location.let {
            Gson().fromJson(it, SetupLocation::class.java).display
        }?.apply {
            showLastSyncTime()
            supportActionBar?.title = this.imageSpan(
                this@HomeActivity, ResourceR.drawable.ic_location_pin, ImageSpanGravity.START
            )
        }
    }

    /**
     * Displays the last data synchronization time as the toolbar subtitle.
     *
     * This method observes the last sync time from the [UserViewModel] and updates
     * the toolbar subtitle with a formatted string indicating the last sync time.
     */
    private fun showLastSyncTime() {
        userViewModel.appLastSyncTime()
        userViewModel.lastSyncData.observe(this) {
            it ?: return@observe
            supportActionBar?.subtitle = getString(ResourceR.string.content_last_synced, it)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawers()
        return when (item.itemId) {
            R.id.nav_achievements -> {
                navController.navigate(R.id.nav_achievements)
                true
            }

            R.id.nav_setting -> {
                navController.navigate(R.id.nav_setting)
                true
            }

            R.id.nav_about_us -> {
                navController.navigate(R.id.nav_about_us)
                true
            }

            R.id.nav_logout -> {
                logoutUser()
                true
            }

            else -> false
        }
    }

    /**
     * Initiates the user logout process.
     *
     * This method displays a confirmation dialog to the user. If the user confirms,
     * it calls the [UserViewModel] to perform the logout and navigates to the
     * [OnboardingActivity].
     */
    private fun logoutUser() {
        showCommonDialog(
            DialogParams(icon = ResourceR.drawable.ic_dialog_alert,
                         title = ResourceR.string.action_logout,
                         message = ResourceR.string.content_are_you_sure_logout,
                         positiveLbl = ResourceR.string.action_logout,
                         negativeLbl = ResourceR.string.action_cancel,
                         onPositiveClick = {
                             userViewModel.logout()
                             gotoNextActivity(OnboardingActivity::class.java, true)
                         })
        )
    }

    /**
     * Observes the user data from the [UserViewModel] and updates the navigation drawer header.
     *
     * This method observes the user data and updates the UI elements in the
     * navigation drawer header with the retrieved user information.
     */
    private fun observeUser() {
        userViewModel.getUser().observe(this) {
            it ?: return@observe
            headerBinding.user = it
        }
    }

    /**
     * Sets up the navigation drawer header view.
     *
     * This method inflates the header view, binds it using data binding, and sets up
     * click listeners for the close drawer button and the edit profile button.
     */
    private fun setupHeaderView() {
        val headerView = binding.navigationView.getHeaderView(0)
        headerBinding = DrawerHomeNavHeaderBinding.bind(headerView)
        headerBinding.ivCloseDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }

        headerBinding.btnEditProfile.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            navController.navigate(R.id.nav_my_profile)
        }
    }

    /**
     * Handles the back button press event.
     *
     * This method overrides the default back button behavior to provide custom
     * navigation logic. It checks if the navigation drawer is open and closes it
     * if it is. Otherwise, it attempts to navigate up in the navigation hierarchy.
     * If the user is already at the top-level "home" destination, it displays a
     * confirmation dialog to exit the application.
     */
    private fun handleBackPressEvent() {
        onBackPressedDispatcher.addCallback(this, true) {
            if (binding.drawerLayout.isOpen) binding.drawerLayout.closeDrawers()
            else if (navController.currentDestination?.id != R.id.nav_home) navController.navigateUp()
            else showCommonDialog(
                DialogParams(icon = ResourceR.drawable.ic_dialog_alert,
                             title = ResourceR.string.dialog_title_exit_app,
                             message = ResourceR.string.content_are_you_sure_exit,
                             positiveLbl = ResourceR.string.action_exit,
                             negativeLbl = ResourceR.string.action_cancel,
                             onPositiveClick = { finish() })
            )
        }
    }

    /**
     * Updates the user's device token on the server.
     *
     * This method calls the [UserViewModel] to send the user's device token
     * to the server. It observes the result of this operation and logs the
     * status of the update using Timber.
     */
    private fun updateDeviceToken() {
        userViewModel.sendUserDeviceToken().observe(this) {
            it ?: return@observe
            Timber.d { "Device token save status: ${it.status.name}" }
        }
    }
}
