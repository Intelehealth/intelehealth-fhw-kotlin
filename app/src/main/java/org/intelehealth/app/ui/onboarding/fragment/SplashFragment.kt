package org.intelehealth.app.ui.onboarding.fragment

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
import androidx.work.WorkInfo
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSplashBinding
import org.intelehealth.app.ui.language.adapter.LanguageAdapter
import org.intelehealth.app.ui.language.fragment.LanguageFragment
import org.intelehealth.app.ui.onboarding.viewmodel.LauncherViewModel
import org.intelehealth.common.extensions.requestNeededPermissions
import org.intelehealth.common.extensions.showCommonDialog
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.config.room.entity.ActiveLanguage

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:20.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SplashFragment : LanguageFragment(R.layout.fragment_splash), BaseViewHolder.ViewHolderClickListener {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var adapter: LanguageAdapter
    private val launcherViewModel by activityViewModels<LauncherViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        handleButtonClickListener()
        initLanguageList()

        binding.tvTitle.isVisible = BuildConfig.FLAVOR_client != "bmgf"

        observeInitialLaunchStatus()
        observeFailResult()
    }

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

    private fun observeFailResult() {
        launcherViewModel.failDataResult.observe(viewLifecycleOwner) { status ->
            if (status == WorkInfo.State.FAILED.name) {
                showConfigFailDialog()
            }
        }
    }

    private fun showConfigFailDialog() {
        DialogParams(icon = org.intelehealth.resource.R.drawable.ic_dialog_alert,
            title = org.intelehealth.resource.R.string.title_error,
            message = org.intelehealth.resource.R.string.content_something_went_wrong,
            positiveLbl = org.intelehealth.resource.R.string.action_retry,
            negativeLbl = org.intelehealth.resource.R.string.action_cancel,
            onPositiveClick = { launcherViewModel.requestConfig() },
            onNegativeClick = { requireActivity().finish() }).let {
            showCommonDialog(it)
        }
    }

    private fun navigateToNextScreen() {
        if (launcherViewModel.isUserLoggedIn()) {
            findNavController().navigate(SplashFragmentDirections.actionSplashToHome())
            requireActivity().finish()
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashToLogin())
        }
    }

    private fun initLanguageList() {
        binding.rvSelectLanguage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSelectLanguage.itemAnimator = DefaultItemAnimator()
        adapter = LanguageAdapter(requireContext(), arrayListOf()).apply {
            this.viewHolderClickListener = this@SplashFragment
            binding.rvSelectLanguage.adapter = this
        }
    }

    override fun onLanguageLoaded(languages: List<ActiveLanguage>) {
        adapter.updateItems(languages.toMutableList())
    }

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

    private fun animateViews() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
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
        }, 500)
    }

    private fun showChooseLanguageUI(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 2000
        transition.addTarget(R.id.layout_panel)
        TransitionManager.beginDelayedTransition(binding.layoutParent, transition)
        binding.layoutPanel.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.layout_rb_choose_language) {
            val lang = view.tag as ActiveLanguage
            adapter.select(position, lang)
        }
    }
}