package org.intelehealth.app.ui.home.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import org.intelehealth.app.ui.onboarding.activity.OnboardingActivity
import org.intelehealth.app.ui.user.viewmodel.UserViewModel
import org.intelehealth.common.extensions.ImageSpanGravity
import org.intelehealth.common.extensions.gotoNextActivity
import org.intelehealth.common.extensions.imageSpan
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.network.model.SetupLocation
import javax.inject.Inject
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 03-01-2025 - 18:34.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Inject preference utils
    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    private val userViewModel: UserViewModel by viewModels()
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
     * Setup navigation controller with toolbar, drawer layout, bottom navigation view
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
     * Change title and subtitle as destination changed
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
     * Display home title with location and last synced time
     */
    private fun displayHomeTitle() {
        preferenceUtils.location.let {
            Gson().fromJson(it, SetupLocation::class.java).display
        }?.apply {
            val lastSyncedTime = preferenceUtils.lastSyncedTime
            val subtitle = getString(ResourceR.string.content_last_synced, lastSyncedTime)
            supportActionBar?.title = this.imageSpan(
                this@HomeActivity, ResourceR.drawable.ic_location_pin, ImageSpanGravity.START
            )
            supportActionBar?.subtitle = subtitle
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        binding.drawerLayout.closeDrawers()
        return when (item.itemId) {
            R.id.nav_setting -> {
                navController.navigate(R.id.nav_setting)
                true
            }

            R.id.nav_logout -> {
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
                true
            }

            else -> false
        }
    }

    private fun observeUser() {
        userViewModel.getUser().observe(this) {
            it ?: return@observe
            headerBinding.user = it
        }
    }

    private fun setupHeaderView() {
        val headerView = binding.navigationView.getHeaderView(0)
        headerBinding = DrawerHomeNavHeaderBinding.bind(headerView)
        headerBinding.ivCloseDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }

        headerBinding.btnEditProfile.setOnClickListener {
//            navController.navigate(R.id.nav_profile)
            binding.drawerLayout.closeDrawers()
        }
    }

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

    private fun updateDeviceToken() {
        userViewModel.sendUserDeviceToken().observe(this) {
            it ?: return@observe
            Timber.d { "Device token save status: ${it.status.name}" }
        }
    }
}