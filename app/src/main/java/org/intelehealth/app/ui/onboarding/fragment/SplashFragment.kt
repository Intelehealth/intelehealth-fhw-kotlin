package org.intelehealth.app.ui.onboarding.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSplashBinding
import org.intelehealth.app.ui.language.adapter.LanguageAdapter
import org.intelehealth.app.ui.language.fragment.LanguageFragment
import org.intelehealth.app.ui.onboarding.activity.OnboardingActivity
import org.intelehealth.app.ui.onboarding.viewmodel.ANIM_DURATION
import org.intelehealth.app.ui.onboarding.viewmodel.LauncherViewModel
import org.intelehealth.app.ui.onboarding.viewmodel.SPLASH_DELAY_TIME
import org.intelehealth.common.extensions.requestNeededPermissions
import org.intelehealth.common.extensions.showNetworkFailureDialog
import org.intelehealth.common.extensions.showRetryDialogOnWentWrong
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.common.utility.API_ERROR
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.config.room.entity.ActiveLanguage

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:20.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * The initial splash screen fragment displayed when the application starts.
 *
 * This fragment handles the following tasks:
 * - Displays the application logo and performs initial animations.
 * - Checks if it's the user's first launch and requests necessary permissions.
 * - Presents a language selection UI if it's the first launch.
 * - Navigates to the appropriate next screen (login or home) based on the user's
 *   authentication status.
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashFragment : LanguageFragment(R.layout.fragment_splash), BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var adapter: LanguageAdapter
    private val launcherViewModel by activityViewModels<LauncherViewModel>()

    /**
     * Called when the fragment's view is created.
     *
     * This method initializes the view binding, sets up the button click
     * listener, and initializes the language list. It also observes the initial
     * launch status and handles any errors that may occur during the process.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        handleButtonClickListener()
        initLanguageList()

        binding.tvTitle.isVisible = BuildConfig.FLAVOR_client != "bmgf"

        observeInitialLaunchStatus()
        observeFailResult()
    }

    /**
     * Sets up the initial launch status observer.
     *
     * This method observes the initial launch status from the [LauncherViewModel]
     * and performs actions based on whether it's the first launch or not.
     */
    private fun observeInitialLaunchStatus() {
        launcherViewModel.initialLaunchStatus.observe(viewLifecycleOwner) { isInitialLaunch ->
            Timber.d { "isInitialLaunch : $isInitialLaunch" }
            if (isInitialLaunch) {
                requestNeededPermissions { }
                animateViews()
            } else {
                requestNeededPermissions { navigateToNextScreen() }
            }
        }
    }

    /**
     * Sets up the language for the app.
     *
     * This method is responsible for setting the current language based on the
     * user's selection. It also initializes the language list and handles the
     * click event for the "Next" button.
     */
    private fun observeFailResult() {
        launcherViewModel.failDataResult.observe(viewLifecycleOwner) { status ->
            if (status == NO_DATA_FOUND) {
                showRetryDialogOnWentWrong({ launcherViewModel.requestConfig() }, { requireActivity().finish() })
            }
        }

        launcherViewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
            if (it.not()) showNetworkFailureDialog {
                (requireActivity() as OnboardingActivity).checkForceUpdate()
            }
        }

        launcherViewModel.errorDataResult.observe(viewLifecycleOwner) {
            if (it.message == API_ERROR) {
                showRetryDialogOnWentWrong({ launcherViewModel.requestConfig() }, { requireActivity().finish() })
            }
        }
    }

    /**
     * Sets up the language for the app.
     *
     * This method is responsible for setting the current language based on the
     * user's selection. It also initializes the language list and handles the
     * click event for the "Next" button.
     */
    private fun navigateToNextScreen() {
        if (launcherViewModel.isUserLoggedIn()) {
            findNavController().navigate(SplashFragmentDirections.actionSplashToHome())
            requireActivity().finish()
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashToLogin())
        }
    }

    /**
     * Sets up the language list and its adapter.
     *
     * This method initializes the RecyclerView for language selection and sets
     * up the adapter to display the available languages. It also sets the click
     * listener for the adapter.
     */
    private fun initLanguageList() {
        binding.rvSelectLanguage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSelectLanguage.itemAnimator = DefaultItemAnimator()
        adapter = LanguageAdapter(requireContext(), arrayListOf()).apply {
            this.viewHolderClickListener = this@SplashFragment
            binding.rvSelectLanguage.adapter = this
        }
    }

    /**
     * Sets up the language selection UI and loads available languages.
     *
     * This method is responsible for displaying the language selection UI and
     * loading the available languages from the [LauncherViewModel]. It also
     * handles the click event for the "Next" button.
     */
    override fun onLanguageLoaded(languages: List<ActiveLanguage>) {
        adapter.updateItems(languages.toMutableList())
    }

    /**
     * Sets up the language selection UI and handles the button click event.
     *
     * This method is responsible for displaying the language selection UI and
     * setting up the click listener for the "Next" button. When a language is
     * selected, it updates the current language preference and navigates to the
     * intro screen.
     */
    private fun handleButtonClickListener() {
        binding.btnNextToIntro.setOnClickListener {
            adapter.getList().find { it.selected }?.let {
                preferenceUtils.currentLanguage = it.code
                Timber.d { "Selected Language : ${preferenceUtils.currentLanguage}" }
                Timber.d { "Selected Language code : ${it.code}" }
                setupLanguage()
                findNavController().navigate(SplashFragmentDirections.actionSplashToIntro())
            }
        }
    }

    /**
     * Performs initial animations on the splash screen views.
     *
     * This method applies a translation animation to the specified view and
     * shows the language selection UI after the animation completes.
     */
    private fun animateViews() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            {
                val translateAnim = AnimationUtils.loadAnimation(
                    requireContext(), org.intelehealth.resource.R.anim.anim_center_to_top
                )
                translateAnim.fillAfter = true
                translateAnim.isFillEnabled = true
                translateAnim.fillBefore = false
                translateAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        showChooseLanguageUI(true)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                binding.layoutChild1.startAnimation(translateAnim)
            }, SPLASH_DELAY_TIME
        )
    }

    /**
     * Shows or hides the language selection UI with a slide-up/down animation.
     *
     * @param show `true` to show the language selection UI, `false` to hide it.
     */
    private fun showChooseLanguageUI(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = ANIM_DURATION
        transition.addTarget(R.id.layout_panel)
        TransitionManager.beginDelayedTransition(binding.layoutParent, transition)
        binding.layoutPanel.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Sets up the language for the app.
     *
     * This method is responsible for setting the current language based on the
     * user's selection. It also initializes the language list and handles the
     * click event for the "Next" button.
     */
    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.layout_rb_choose_language) {
            val lang = view.tag as ActiveLanguage
            adapter.select(position, lang)
        }
    }
}
