package org.intelehealth.app.ui.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentChangeLanguageBinding
import org.intelehealth.app.ui.language.adapter.LanguageAdapter
import org.intelehealth.app.ui.language.fragment.LanguageFragment
import org.intelehealth.app.ui.setting.viewmodel.SettingViewModel
import org.intelehealth.common.extensions.setupLinearView
import org.intelehealth.common.ui.viewholder.BaseViewHolder
import org.intelehealth.config.presenter.language.viewmodel.LanguageViewModel
import org.intelehealth.config.room.entity.ActiveLanguage

/**
 * Created by Vaghela Mithun R. on 19-03-2025 - 12:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that allows the user to change the application's language.
 *
 * This fragment displays a list of available languages and allows the user to
 * select a new language. It interacts with the [SettingViewModel] to update
 * the selected language.
 */
@AndroidEntryPoint
class ChangeLanguageFragment : LanguageFragment(R.layout.fragment_change_language),
    BaseViewHolder.ViewHolderClickListener {

    private lateinit var binding: FragmentChangeLanguageBinding
    private lateinit var adapter: LanguageAdapter
    private val settingViewModel by activityViewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeLanguageBinding.bind(view)
        setupLanguageListView()
        handleChangeLanguageClickEvent()
    }

    private fun observeCurrentLang() {
        settingViewModel.languageStateData.observe(viewLifecycleOwner) {
            if (::adapter.isInitialized) adapter.setDefaultLang(it)
        }
    }

    /**
     * Handles the click event for the "Change Language" button.
     *
     * This method retrieves the selected language from the adapter, updates the
     * language in the [SettingViewModel], applies the new language, and navigates
     * back to the previous screen.
     */
    private fun handleChangeLanguageClickEvent() {
        binding.btnChangeLanguage.setOnClickListener {
            adapter.getList().find { it.selected }?.let {
                settingViewModel.setLanguage(it.code)
                setupLanguage()
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Sets up the RecyclerView to display the list of languages.
     *
     * This method initializes the [LanguageAdapter], sets the click listener,
     * and configures the RecyclerView with a linear layout and the adapter.
     */
    private fun setupLanguageListView() {
        adapter = LanguageAdapter(requireContext(), emptyList())
        adapter.viewHolderClickListener = this
        binding.rvLanguageList.setupLinearView(adapter, true)
    }

    override fun onLanguageLoaded(languages: List<ActiveLanguage>) {
        super.onLanguageLoaded(languages)
        adapter.updateItems(languages.toMutableList())
        observeCurrentLang()
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.layout_rb_choose_language) {
            val lang = view.tag as ActiveLanguage
            adapter.select(position, lang)
        }
    }
}
